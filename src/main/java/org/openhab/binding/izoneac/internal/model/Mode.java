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
 * Air-conditioning mode
 *
 * @author Thomas Tan - Initial contribution
 */
public enum Mode {
    AUTO("auto"),
    VENT("vent"),
    COOL("cool"),
    DRY("dry"),
    HEAT("heat");

    private String value;

    Mode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Mode fromValue(String value) {
        for (Mode mode : values()) {
            if (mode.value.equals(value)) {
                return mode;
            }
        }

        return null;
    }
}
