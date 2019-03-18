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
package org.openhab.binding.izoneac.internal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The {@link Controller} is a model object to contain controller properties
 * handlers.
 *
 * @author Thomas Tan - Initial contribution
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Controller {
    @JsonProperty("AirStreamDeviceUId")
    private String id;

    @JsonProperty("Tag1")
    private String name;

    @JsonProperty("UnitType")
    private String unitType;

    @JsonProperty("SysOn")
    private String powerStatus;

    @JsonProperty("Setpoint")
    private String setpoint;

    @JsonProperty("Temp")
    private String temperature;

    @JsonProperty("SysMode")
    private String mode;

    @JsonProperty("SysFan")
    private String fanSpeed;

    @JsonProperty("SleepTimer")
    private Integer sleepTimer;

    @JsonProperty("FreeAir")
    private String freeAir;

    @JsonProperty("FavouriteSet")
    private Integer favouriteSet;

    @JsonProperty("NoOfZones")
    private Integer zones;

    @JsonProperty("NoOfConst")
    private Integer constants;

    @JsonProperty("Warnings")
    private String warnings;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnitType() {
        return unitType;
    }

    public OnOffStatus getPowerStatus() {
        return OnOffStatus.fromValue(powerStatus);
    }

    public Double getSetpoint() {
        return Double.parseDouble(setpoint);
    }

    public Double getTemperature() {
        return Double.parseDouble(temperature);
    }

    public Integer getConstants() {
        return constants;
    }

    public Mode getMode() {
        return Mode.fromValue(mode);
    }

    public FanSpeed getFanSpeed() {
        return FanSpeed.fromValue(fanSpeed);
    }

    public Integer getSleepTimer() {
        return sleepTimer;
    }

    public OnOffStatus getFreeAir() {
        return OnOffStatus.fromValue(freeAir);
    }

    public Integer getFavouriteSet() {
        return favouriteSet;
    }

    public Integer getZones() {
        return zones;
    }

    public String getWarnings() {
        return warnings;
    }

    @Override
    public String toString() {
        return "Controller [id=" + id + ", name=" + name + ", unitType=" + unitType + ", powerStatus=" + powerStatus
                + ", setpoint=" + setpoint + ", temperature=" + temperature + ", mode=" + mode + ", fanSpeed="
                + fanSpeed + ", sleepTimer=" + sleepTimer + ", freeAir=" + freeAir + ", favouriteSet=" + favouriteSet
                + ", zones=" + zones + ", constants=" + constants + ", warnings=" + warnings + "]";
    }
}
