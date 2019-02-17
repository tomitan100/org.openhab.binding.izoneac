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
 * Air-condition on/off status.
 *
 * @author Thomas Tan - Initial contribution
 */
public enum OnOffStatus {
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
