/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.izoneac.internal.config;

/**
 * The {@link ControllerConfiguration} is responsible for holding configuration information of iZone AC Controller.
 *
 * @author Thomas Tan - Initial contribution
 */
public class ControllerConfiguration {
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String REFRESH = "refresh";

    public String networkAddress;
    public int networkPort = 80;
    public int refresh = 30;
}
