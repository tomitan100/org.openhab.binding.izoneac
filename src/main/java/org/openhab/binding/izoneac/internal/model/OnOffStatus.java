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
 * Air-condition on/off status.
 *
 * @author Thomas Tan - Initial contribution
 */
public enum OnOffStatus {
    DISABLED("disabled"),
    ON("on"),
    OFF("off");

    private String value;

    OnOffStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OnOffStatus fromValue(String value) {
        for (OnOffStatus powerStatus : values()) {
            if (powerStatus.value.equals(value)) {
                return powerStatus;
            }
        }

        return null;
    }
}
