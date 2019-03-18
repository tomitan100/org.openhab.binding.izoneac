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
 * Air-condition vent position.
 *
 * @author Thomas Tan - Initial contribution
 */
public enum VentPosition {
    AUTO("auto"),
    OPEN("open"),
    CLOSE("close");

    private String value;

    VentPosition(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static VentPosition fromValue(String value) {
        for (VentPosition ventPosition : values()) {
            if (ventPosition.value.equals(value)) {
                return ventPosition;
            }
        }

        return null;
    }
}
