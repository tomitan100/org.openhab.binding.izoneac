/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
