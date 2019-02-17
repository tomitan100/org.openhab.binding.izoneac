/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.izoneac.internal.config;

/**
 * The {@link ControllerConfiguration} is responsible for holding configuration information of iZone Controller.
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
