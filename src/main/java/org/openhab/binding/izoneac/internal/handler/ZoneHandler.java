/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.izoneac.internal.handler;

import org.openhab.binding.izoneac.internal.config.ZoneConfiguration;
import org.openhab.binding.izoneac.internal.iZoneAcBindingConstants;
import org.openhab.binding.izoneac.internal.iZoneAcClientError;
import org.openhab.binding.izoneac.internal.model.Zone;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ZoneHandler} is responsible for handling commands for a zone.
 *
 * @author Thomas Tan - Initial contribution
 */
public class ZoneHandler extends BaseThingHandler {
    private final Logger logger = LoggerFactory.getLogger(ZoneHandler.class);

    private ZoneConfiguration config;

    public ZoneHandler(Thing thing) {
        super(thing);
    }

    private ControllerHandler getControllerHandler() {
        Bridge bridge = getBridge();
        return bridge != null ? (ControllerHandler) bridge.getHandler() : null;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (!(command instanceof RefreshType)) {
            ControllerHandler controllerHandler = getControllerHandler();

            try {
                switch (channelUID.getIdWithoutGroup()) {
                    case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_VENT_POSITION:
                        controllerHandler.sendZoneCommand(this.getThing().getUID().getId(), "ZoneCommand",
                                command.toString().toLowerCase());
                        break;
                    case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_MIN_AIRFLOW:
                        controllerHandler.sendZoneCommand(this.getThing().getUID().getId(), "AirMinCommand",
                                command.toString());
                        break;
                    case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_MAX_AIRFLOW:
                        controllerHandler.sendZoneCommand(this.getThing().getUID().getId(), "AirMaxCommand",
                                command.toString());
                        break;
                    case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_SETPOINT:
                        controllerHandler.sendZoneCommand(this.getThing().getUID().getId(), "SetPoint",
                                command.toString());
                        break;
                }
            } catch (iZoneAcClientError ex) {
                logger.warn("Unable to execute zone command \"" + command.toString() + "\" to channel \""
                        + channelUID.toString() + "\"");
            }
        }
    }

    @Override
    public void initialize() {
        config = getConfigAs(ZoneConfiguration.class);
        updateStatus(ThingStatus.ONLINE);
    }

    public void refresh() {
        // Get from bridge/controller and update state
        ControllerHandler handler = getControllerHandler();

        Zone zone = handler.getZones().stream().filter(z -> z.getId().toString().equals(this.thing.getUID().getId()))
                .findFirst().orElse(null);

        if (zone != null) {
            updateZoneState(zone);
        } else {
            logger.warn("Cannot find zone ID " + this.thing.getUID().getId());
        }
    }

    private void updateZoneState(Zone zone) {
        getThing().getChannels().forEach(channel -> {
            switch (channel.getUID().getIdWithoutGroup()) {
                case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_ID:
                    updateChannelState(channel.getUID(), zone.getId().toString());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_NAME:
                    updateChannelState(channel.getUID(), zone.getName());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_VENT_POSITION:
                    updateChannelState(channel.getUID(), zone.getVentPosition().name());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_MIN_AIRFLOW:
                    updateChannelState(channel.getUID(), zone.getMinAirflow());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_MAX_AIRFLOW:
                    updateChannelState(channel.getUID(), zone.getMaxAirflow());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_SETPOINT:
                    updateChannelState(channel.getUID(), zone.getSetpoint());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_TEMPERATURE:
                    updateChannelState(channel.getUID(), zone.getTemperature());
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_TYPE:
                    updateChannelState(channel.getUID(), zone.getType().getDescription());
                    break;
            }
        });
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
}
