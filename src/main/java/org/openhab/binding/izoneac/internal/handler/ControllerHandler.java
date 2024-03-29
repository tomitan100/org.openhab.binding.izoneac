/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 * <p>
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 * <p>
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.izoneac.internal.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.izoneac.internal.config.ControllerConfiguration;
import org.openhab.binding.izoneac.internal.iZoneAcBindingConstants;
import org.openhab.binding.izoneac.internal.iZoneAcClientError;
import org.openhab.binding.izoneac.internal.model.Controller;
import org.openhab.binding.izoneac.internal.model.OnOffStatus;
import org.openhab.binding.izoneac.internal.model.Zone;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Bridge to access a iZone unit.
 *
 * A single iZone can have one or more zones each with a unique UID.
 *
 * @author Thomas Tan - Initial contribution
 */
public class ControllerHandler extends BaseBridgeHandler {
    private final Logger logger = LoggerFactory.getLogger(ControllerHandler.class);

    private final Object refreshLock = new Object();
    private final Object commandLock = new Object();
    private final ObjectMapper mapper = new ObjectMapper();

    private ScheduledFuture<?> refreshJob;
    private ControllerConfiguration configuration;
    private Controller controller = null;
    private List<Zone> zones = null;

    public ControllerHandler(Bridge thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        logger.info("Initializing iZone AC Controller handler...");
        configuration = getConfigAs(ControllerConfiguration.class);
        startRefresh();
    }

    @Override
    public void dispose() {
        stopRefresh();
        super.dispose();
    }

    private void startRefresh() {
        synchronized (refreshLock) {
            logger.info("Scheduling new refresh job");
            refreshJob = scheduler.scheduleWithFixedDelay(this::refreshSystem, 0, configuration.refresh,
                    TimeUnit.SECONDS);
        }
    }

    private void stopRefresh() {
        synchronized (refreshLock) {
            ScheduledFuture<?> localRefreshJob = refreshJob;
            if (localRefreshJob != null && !localRefreshJob.isCancelled()) {
                logger.info("Cancelling existing refresh job");
                localRefreshJob.cancel(true);
                refreshJob = null;
            }
        }
    }

    private void refreshSystem() {
        try {
            controller = retrieveControllerState();
            updateControllerState();

            zones = retrieveZones();

            updateStatus(ThingStatus.ONLINE);

            getThing().getThings().forEach(thing -> {
                ZoneHandler handler = (ZoneHandler) thing.getHandler();

                if (handler != null) {
                    handler.refresh();
                }
            });
        } catch (iZoneAcClientError ex) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, ex.getMessage());
        }
    }

    private void updateControllerState() {
        getThing().getChannels().forEach(channel -> {
            switch (channel.getUID().getIdWithoutGroup()) {
                case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_ID:
                    updateChannelState(channel.getUID(), controller.getId());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_NAME:
                    updateChannelState(channel.getUID(), controller.getName());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_UNIT_TYPE:
                    updateChannelState(channel.getUID(), controller.getUnitType());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_POWER:
                    updateChannelState(channel.getUID(), controller.getPowerStatus());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_SETPOINT:
                    updateChannelState(channel.getUID(), controller.getSetpoint());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_TEMPERATURE:
                    updateChannelState(channel.getUID(), controller.getTemperature());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_MODE:
                    updateChannelState(channel.getUID(), controller.getMode().name());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_FAN_SPEED:
                    updateChannelState(channel.getUID(), controller.getFanSpeed().name());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_SLEEP_TIMER:
                    updateChannelState(channel.getUID(), controller.getSleepTimer());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_FREE_AIR:
                    if (controller.getFreeAir() != OnOffStatus.DISABLED) {
                        updateChannelState(channel.getUID(), controller.getFreeAir());
                    }
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_CONSTANTS:
                    updateChannelState(channel.getUID(), controller.getConstants());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_ZONES:
                    updateChannelState(channel.getUID(), controller.getZones());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_WARNINGS:
                    updateChannelState(channel.getUID(), controller.getWarnings());
                    break;
            }
        });
    }

    private Controller retrieveControllerState() throws iZoneAcClientError {
        String url = buildUrl("/SystemSettings");

        try {
            return mapper.readValue(new URL(url), Controller.class);
        } catch (Exception ex) {
            logger.error("Error in processing JSON from \"" + url + "\"", ex);
            throw new iZoneAcClientError("Error in processing JSON from \"" + url + "\"" + ex);
        }
    }

    private List<Zone> retrieveZones() throws iZoneAcClientError {
        List<Zone> zones = new ArrayList<>();

        for (int i = 0; i < controller.getZones(); i += 4) {
            zones.addAll(retrieveZoneGroup(i + 1, i + 4));
        }

        return zones;
    }

    private List<Zone> retrieveZoneGroup(int zoneStart, int zoneEnd) throws iZoneAcClientError {
        String url = buildUrl("/Zones" + zoneStart + "_" + zoneEnd);

        try {
            return mapper.readValue(new URL(url), new TypeReference<List<Zone>>() {
            });
        } catch (Exception ex) {
            logger.error("Error in processing JSON from \"" + url + "\"", ex);
            throw new iZoneAcClientError("Error in processing JSON from \"" + url + "\"" + ex);
        }
    }

    public void sendControllerCommand(String command, String value) throws iZoneAcClientError {
        synchronized (commandLock) {
            postCommand(buildUrl("/" + command), "{ \"" + command + "\": \"" + value + "\" }");
        }
    }

    public void sendControllerCommandAsNumber(String command, String value) throws iZoneAcClientError {
        synchronized (commandLock) {
            postCommand(buildUrl("/" + command), "{ \"" + command + "\": " + value + " }");
        }
    }

    public void sendZoneCommand(String zone, String command, String value) throws iZoneAcClientError {
        String zoneNo = Integer.toString(Integer.parseInt(zone) + 1);

        synchronized (commandLock) {
            postCommand(buildUrl("/" + command),
                    "{ \"" + command + "\": { \"ZoneNo\": \"" + zoneNo + "\", \"Command\": \"" + value + "\" } }");
        }
    }

    public void sendZoneCommandAsNumber(String zone, String command, String value) throws iZoneAcClientError {
        String zoneNo = Integer.toString(Integer.parseInt(zone) + 1);

        synchronized (commandLock) {
            postCommand(buildUrl("/" + command),
                    "{ \"" + command + "\": { \"ZoneNo\": \"" + zoneNo + "\", \"Command\": " + value + " } }");
        }
    }

    private @Nullable String buildUrl(String path) {
        return "http://" + configuration.networkAddress + ":" + configuration.networkPort + path;
    }

    private boolean postCommand(String url, String jsonCommand) {
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) httpUrl.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            byte[] request = jsonCommand.getBytes();
            os.write(request, 0, request.length);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;

                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
        } catch (IOException ex) {
            logger.error("Unable to post command \"" + jsonCommand + "\" to \"" + url + "\"");
            return false;
        }

        return true;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (!(command instanceof RefreshType)) {
            try {
                switch (channelUID.getIdWithoutGroup()) {
                    case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_POWER:
                        sendControllerCommand("SystemON", command.toString().toLowerCase());
                        break;
                    case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_MODE:
                        sendControllerCommand("SystemMODE", command.toString().toLowerCase());
                        break;
                    case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_FAN_SPEED:
                        sendControllerCommand("SystemFAN", command.toString().toLowerCase());
                        break;
                    case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_SETPOINT:
                        sendControllerCommand("UnitSetpoint", command.toString());
                        break;
                    case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_FREE_AIR:
                        if (controller.getFreeAir() != OnOffStatus.DISABLED) {
                            sendControllerCommand("FreeAir", command.toString().toLowerCase());
                        }
                        break;
                    case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_SLEEP_TIMER:
                        sendControllerCommandAsNumber("SleepTimer", command.toString());
                        break;
                    case iZoneAcBindingConstants.CHANNEL_TYPE_CONTROLLER_FAVOURITE_SET:
                        sendControllerCommand("FavouriteSet", command.toString());
                        break;
                }
            } catch (iZoneAcClientError ex) {
                logger.warn("Unable to execute command \"" + command.toString() + "\" to channel \""
                        + channelUID.toString() + "\"");
            }
        }
    }

    private void updateChannelState(@NonNull ChannelUID uid, OnOffStatus status) {
        if (status != null) {
            updateState(uid, OnOffType.from(status.getValue()));
        }
    }

    private void updateChannelState(ChannelUID uid, String value) {
        if (value != null) {
            updateState(uid, new StringType(value));
        }
    }

    private void updateChannelState(ChannelUID uid, Double value) {
        if (value != null && !value.isNaN()) {
            updateState(uid, new DecimalType(value));
        }
    }

    private void updateChannelState(ChannelUID uid, Integer value) {
        if (value != null) {
            updateState(uid, new DecimalType(value));
        }
    }

    public Controller getController() {
        return controller;
    }

    public List<Zone> getZones() {
        return zones;
    }
}
