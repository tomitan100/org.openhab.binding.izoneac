/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.izoneac.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link iZoneAcBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Thomas Tan - Initial contribution
 */
@NonNullByDefault
public class iZoneAcBindingConstants {

    private static final String BINDING_ID = "izoneac";

    public static final ThingTypeUID THING_TYPE_CONTROLLER = new ThingTypeUID(BINDING_ID, "controller");
    public static final ThingTypeUID THING_TYPE_ZONE = new ThingTypeUID(BINDING_ID, "zone");

    // Controller channels
    public static final String CHANNEL_TYPE_CONTROLLER_ID = "id";
    public static final String CHANNEL_TYPE_CONTROLLER_NAME = "name";
    public static final String CHANNEL_TYPE_CONTROLLER_UNIT_TYPE = "unitType";
    public static final String CHANNEL_TYPE_CONTROLLER_POWER = "power";
    public static final String CHANNEL_TYPE_CONTROLLER_SETPOINT = "setpoint";
    public static final String CHANNEL_TYPE_CONTROLLER_TEMPERATURE = "temperature";
    public static final String CHANNEL_TYPE_CONTROLLER_MODE = "mode";
    public static final String CHANNEL_TYPE_CONTROLLER_FAN_SPEED = "fanSpeed";
    public static final String CHANNEL_TYPE_CONTROLLER_FREE_AIR = "freeAir";
    public static final String CHANNEL_TYPE_CONTROLLER_SLEEP_TIMER = "sleepTimer";
    public static final String CHANNEL_TYPE_CONTROLLER_FAVOURITE_SET = "favouriteSet";
    public static final String CHANNEL_TYPE_CONTROLLER_ZONES = "zones";
    public static final String CHANNEL_TYPE_CONTROLLER_CONSTANTS = "constants";
    public static final String CHANNEL_TYPE_CONTROLLER_WARNINGS = "warnings";

    // Zone channels
    public static final String CHANNEL_TYPE_ZONE_ID = "id";
    public static final String CHANNEL_TYPE_ZONE_NAME = "name";
    public static final String CHANNEL_TYPE_ZONE_VENT_POSITION = "ventPosition";
    public static final String CHANNEL_TYPE_ZONE_MIN_AIRFLOW = "minAirflow";
    public static final String CHANNEL_TYPE_ZONE_MAX_AIRFLOW = "maxAirflow";
    public static final String CHANNEL_TYPE_ZONE_SETPOINT = "setpoint";
    public static final String CHANNEL_TYPE_ZONE_TEMPERATURE = "temperature";
    public static final String CHANNEL_TYPE_ZONE_TYPE = "type";
}
