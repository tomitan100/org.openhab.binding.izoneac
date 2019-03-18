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
 * Air-condition zone type.
 *
 * @author Thomas Tan - Initial contribution
 */
public enum ZoneType {
    AUTO("auto", "Auto"),
    CONSTANT("const", "Constant"),
    OPEN_CLOSE("opcl", "Toggle");

    private String value;
    private String description;

    ZoneType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ZoneType fromValue(String value) {
        for (ZoneType zoneType : values()) {
            if (zoneType.value.equals(value)) {
                return zoneType;
            }
        }

        return null;
    }
}
