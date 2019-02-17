/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.izoneac.internal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The {@link Zone} is responsible for creating things and thing
 * handlers.
 *
 * @author Thomas Tan - Initial contribution
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Zone {
    @JsonProperty("Index")
    private Integer id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Mode")
    private String ventPosition;

    @JsonProperty("MinAir")
    private Integer minAirflow;

    @JsonProperty("MaxAir")
    private Integer maxAirflow;

    @JsonProperty("SetPoint")
    private Double setpoint;

    @JsonProperty("Temp")
    private Double temperature;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Const")
    private Integer constantId;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public VentPosition getVentPosition() {
        return VentPosition.fromValue(ventPosition);
    }

    public Integer getMinAirflow() {
        return minAirflow;
    }

    public Integer getMaxAirflow() {
        return maxAirflow;
    }

    public Double getSetpoint() {
        return setpoint;
    }

    public Double getTemperature() {
        return temperature;
    }

    public ZoneType getType() {
        return ZoneType.fromValue(type);
    }

    @Override
    public String toString() {
        return "Zone [id=" + id + ", name=" + name + ", ventPosition=" + ventPosition + ", minAirflow=" + minAirflow
                + ", maxAirflow=" + maxAirflow + ", setpoint=" + setpoint + ", temperature=" + temperature + ", type="
                + type + ", constantId=" + constantId + "]";
    }
}

/*
 * "AirStreamDeviceUId": "000000720",
 * "Id": 0,
 * "Index": 0,
 * "Name": "Study",
 * "Type": "opcl",
 * "Mode": "open",
 * "SetPoint": 23,
 * "Temp": 0,
 * "MaxAir": 100,
 * "MinAir": 0,
 * "Const": 255,
 * "ConstA": "false"
 */

// [{
// "AirStreamDeviceUId": "000000720",
// "Id": 0,
// "Index": 0,
// "Name": "Study",
// "Type": "opcl",
// "Mode": "open",
// "SetPoint": 23,
// "Temp": 0,
// "MaxAir": 100,
// "MinAir": 0,
// "Const": 255,
// "ConstA": "false"
// }, {
// "AirStreamDeviceUId": "000000720",
// "Id": 0,
// "Index": 1,
// "Name": "Theatre",
// "Type": "opcl",
// "Mode": "close",
// "SetPoint": 23,
// "Temp": 0,
// "MaxAir": 100,
// "MinAir": 0,
// "Const": 255,
// "ConstA": "false"
// }, {
// "AirStreamDeviceUId": "000000720",
// "Id": 0,
// "Index": 2,
// "Name": "Living",
// "Type": "opcl",
// "Mode": "open",
// "SetPoint": 23,
// "Temp": 0,
// "MaxAir": 100,
// "MinAir": 0,
// "Const": 255,
// "ConstA": "false"
// }, {
// "AirStreamDeviceUId": "000000720",
// "Id": 0,
// "Index": 3,
// "Name": "Activity",
// "Type": "opcl",
// "Mode": "open",
// "SetPoint": 23,
// "Temp": 0,
// "MaxAir": 100,
// "MinAir": 0,
// "Const": 255,
// "ConstA": "false"
// }]