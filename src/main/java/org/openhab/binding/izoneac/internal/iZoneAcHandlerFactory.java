/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.izoneac.internal;

import static org.openhab.binding.izoneac.internal.iZoneAcBindingConstants.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.izoneac.internal.discovery.iZoneAcDiscoveryService;
import org.openhab.binding.izoneac.internal.handler.ControllerHandler;
import org.openhab.binding.izoneac.internal.handler.ZoneHandler;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link iZoneAcHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Thomas Tan - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.izoneac", service = ThingHandlerFactory.class)
public class iZoneAcHandlerFactory extends BaseThingHandlerFactory {
    private final Logger logger = LoggerFactory.getLogger(iZoneAcHandlerFactory.class);

    private final Map<ThingUID, @Nullable ServiceRegistration<?>> discoveryServiceRegs = new HashMap<>();

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return THING_TYPE_CONTROLLER.equals(thingTypeUID) || THING_TYPE_ZONE.equals(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        if (THING_TYPE_CONTROLLER.equals(thingTypeUID)) {
            ControllerHandler handler = new ControllerHandler((Bridge) thing);
            registerDeviceDiscoveryService(handler);

            return handler;
        } else if (THING_TYPE_ZONE.equals(thingTypeUID)) {
            return new ZoneHandler(thing);
        }

        return null;
    }

    @Override
    protected synchronized void removeHandler(ThingHandler thingHandler) {
        if (thingHandler instanceof ControllerHandler) {
            ServiceRegistration<?> serviceReg = discoveryServiceRegs.remove(thingHandler.getThing().getUID());
            if (serviceReg != null) {
                serviceReg.unregister();
            }
        }
    }

    private synchronized void registerDeviceDiscoveryService(ControllerHandler handler) {
        iZoneAcDiscoveryService discoveryService = new iZoneAcDiscoveryService(handler);

        discoveryServiceRegs.put(handler.getThing().getUID(),
                bundleContext.registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<>()));
    }
}
