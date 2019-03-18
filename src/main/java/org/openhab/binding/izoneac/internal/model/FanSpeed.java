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

/**
 * Fan speed
 *
 * @author Thomas Tan - Initial contribution
 */
public enum FanSpeed {
    LOW("low"),
    MEDIUM("med"),
    HIGH("high"),
    AUTO("auto");

    private String value;

    FanSpeed(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FanSpeed fromValue(String value) {
        for (FanSpeed fanSpeed : values()) {
            if (fanSpeed.value.equals(value)) {
                return fanSpeed;
            }
        }

        return null;
    }
}
