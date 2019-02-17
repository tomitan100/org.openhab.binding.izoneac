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
