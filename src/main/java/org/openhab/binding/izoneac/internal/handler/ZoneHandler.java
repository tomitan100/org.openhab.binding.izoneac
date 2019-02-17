/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.izoneac.internal.handler;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.izoneac.internal.iZoneAcBindingConstants;
import org.openhab.binding.izoneac.internal.iZoneAcClientError;
import org.openhab.binding.izoneac.internal.config.ZoneConfiguration;
import org.openhab.binding.izoneac.internal.model.Zone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ZoneHandler} is responsible for handling commands for a zone.
 *
 * @author Thomas Tan - Initial contribution
 */
public class ZoneHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(ZoneHandler.class);

    @Nullable
    private ZoneConfiguration configuration;

    public ZoneHandler(Thing thing) {
        super(thing);
    }

    private @Nullable ControllerHandler getControllerHandler() {
        Bridge bridge = getBridge();
        return bridge != null ? (ControllerHandler) bridge.getHandler() : null;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
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
            }
        } catch (iZoneAcClientError ex) {
            logger.warn("Unable to execute zone command \"" + command.toString() + "\" to channel \""
                    + channelUID.toString() + "\"");
        }
    }

    @Override
    public void initialize() {
        configuration = getConfigAs(ZoneConfiguration.class);
        updateStatus(ThingStatus.ONLINE);
    }

    public void refresh() {
        // Get from bridge/controller and update state
        ControllerHandler handler = getControllerHandler();

        Zone zone = handler.getZones().stream().filter(z -> z.getId().toString().equals(this.thing.getUID().getId()))
                .findFirst().get();

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
                    updateState(channel.getUID(), new StringType(zone.getId().toString()));
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_NAME:
                    updateState(channel.getUID(), new StringType(zone.getName()));
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_VENT_POSITION:
                    updateState(channel.getUID(), new StringType(zone.getVentPosition().name()));
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_MIN_AIRFLOW:
                    updateState(channel.getUID(), new DecimalType(zone.getMinAirflow()));
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_MAX_AIRFLOW:
                    updateState(channel.getUID(), new DecimalType(zone.getMaxAirflow()));
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_SETPOINT:
                    updateState(channel.getUID(), new DecimalType(zone.getSetpoint()));
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_TEMPERATURE:
                    updateState(channel.getUID(), new DecimalType(zone.getTemperature()));
                    break;
                case iZoneAcBindingConstants.CHANNEL_TYPE_ZONE_TYPE:
                    updateState(channel.getUID(), new StringType(zone.getType().getDescription()));
                    break;
            }
        });
    }
}
