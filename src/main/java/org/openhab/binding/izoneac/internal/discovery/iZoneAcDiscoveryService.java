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
package org.openhab.binding.izoneac.internal.discovery;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openhab.binding.izoneac.internal.config.ZoneConfiguration;
import org.openhab.binding.izoneac.internal.handler.ControllerHandler;
import org.openhab.binding.izoneac.internal.iZoneAcBindingConstants;
import org.openhab.binding.izoneac.internal.model.Controller;
import org.openhab.binding.izoneac.internal.model.Zone;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ZoneConfiguration} is responsible for holding configuration information for a zone
 *
 * @author Thomas Tan - Initial contribution
 */
public class iZoneAcDiscoveryService extends AbstractDiscoveryService {
    private final Logger logger = LoggerFactory.getLogger(iZoneAcDiscoveryService.class);

    private static int DEFAULT_TIMEOUT_SECONDS = 20;

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = Collections.unmodifiableSet(
            Stream.of(iZoneAcBindingConstants.THING_TYPE_CONTROLLER, iZoneAcBindingConstants.THING_TYPE_ZONE)
                    .collect(Collectors.toSet()));

    private ControllerHandler controllerHandler;

    public iZoneAcDiscoveryService(ControllerHandler controllerHandler) {
        super(SUPPORTED_THING_TYPES, DEFAULT_TIMEOUT_SECONDS);

        logger.info("Initialising iZone AC discovery service");

        this.controllerHandler = controllerHandler;

        this.activate(null);
    }

    @Override
    protected void startScan() {
        logger.debug("Starting iZone AC discovery");
        scheduler.execute(iZoneAcDiscoveryRunnable);
    }

    private Runnable iZoneAcDiscoveryRunnable = () -> {
        Controller controller = controllerHandler.getController();
        List<Zone> zones = controllerHandler.getZones();

        if (controller != null && zones != null) {
            ThingUID bridgeUID = controllerHandler.getThing().getUID();

            // Bridge
            logger.info("Discovered iZone AC controller \"" + controller.getId() + "\" UID: " + bridgeUID);
            thingDiscovered(DiscoveryResultBuilder.create(bridgeUID).build());

            // Zones
            zones.forEach(zone -> {
                logger.info("Discovered iZone AC zone " + zone.getId() + ": " + zone.getName());
                ThingUID thingUID = new ThingUID(iZoneAcBindingConstants.THING_TYPE_ZONE, bridgeUID,
                        zone.getId().toString());
                Map<String, Object> properties = new HashMap<>();
                String zoneId = Integer.toString(zone.getId() + 1).toString();

                properties.put("zoneId", zoneId);

                thingDiscovered(DiscoveryResultBuilder.create(thingUID).withProperties(properties).withBridge(bridgeUID)
                        .withLabel("iZone AC Zone " + zoneId + ": " + zone.getName()).build());
            });
        } else {
            logger.debug("iZone AC controller/zones are not available");
        }
    };
}
