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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.izoneac.internal.config.ZoneConfiguration;
import org.openhab.binding.izoneac.internal.handler.ControllerHandler;
import org.openhab.binding.izoneac.internal.iZoneAcBindingConstants;
import org.openhab.binding.izoneac.internal.model.Controller;
import org.openhab.binding.izoneac.internal.model.Zone;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ZoneConfiguration} is responsible for holding configuration information for a zone
 *
 * @author Thomas Tan - Initial contribution
 */
@NonNullByDefault
@Component(service = DiscoveryService.class)
public class iZoneAcDiscoveryService extends AbstractDiscoveryService {
    private final Logger logger = LoggerFactory.getLogger(iZoneAcDiscoveryService.class);

    private static final int DEFAULT_TIMEOUT_SECONDS = 20;
    private static final int CHECK_INTERVAL = 3600;

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = Set.of(iZoneAcBindingConstants.THING_TYPE_CONTROLLER,
            iZoneAcBindingConstants.THING_TYPE_ZONE);

    @Nullable
    private ControllerHandler controllerHandler;

    @Nullable
    private ScheduledFuture<?> iZoneAcDiscoveryJob;

    public iZoneAcDiscoveryService() {
        super(SUPPORTED_THING_TYPES, DEFAULT_TIMEOUT_SECONDS);
    }

    public iZoneAcDiscoveryService(ControllerHandler controllerHandler) {
        super(SUPPORTED_THING_TYPES, DEFAULT_TIMEOUT_SECONDS);
        this.controllerHandler = controllerHandler;
        activate(null);
    }

    @Override
    protected void startScan() {
        logger.info("Starting iZone AC discovery");
    }

    @Override
    protected void startBackgroundDiscovery() {
        if (controllerHandler != null) {
            if (iZoneAcDiscoveryJob == null) {
                iZoneAcDiscoveryJob = scheduler.scheduleWithFixedDelay(() -> {
                    scanForIZoneThings();
                }, 0, CHECK_INTERVAL, TimeUnit.SECONDS);
            }
        }
    }

    @Override
    protected void stopBackgroundDiscovery() {
        logger.debug("Stopping iZone AC discovery");
        ScheduledFuture<?> discoveryJob = iZoneAcDiscoveryJob;

        if (discoveryJob != null) {
            discoveryJob.cancel(true);
        }

        iZoneAcDiscoveryJob = null;
    }

    private void scanForIZoneThings() {
        Controller controller = controllerHandler.getController();
        List<Zone> zones = controllerHandler.getZones();

        if (controller != null && zones != null) {
            ThingUID bridgeUID = controllerHandler.getThing().getUID();

            // Bridge
            logger.debug("Discovered iZone AC controller \"" + controller.getId() + "\" UID: " + bridgeUID);
            thingDiscovered(DiscoveryResultBuilder.create(bridgeUID).build());

            // Zones
            zones.forEach(zone -> {
                logger.debug("Discovered iZone AC zone " + zone.getId() + ": " + zone.getName());
                ThingUID thingUID = new ThingUID(iZoneAcBindingConstants.THING_TYPE_ZONE, bridgeUID,
                        zone.getId().toString());
                Map<String, Object> properties = new HashMap<>();
                String zoneId = Integer.toString(zone.getId() + 1);

                properties.put("zoneId", zoneId);

                thingDiscovered(DiscoveryResultBuilder.create(thingUID).withProperties(properties).withBridge(bridgeUID)
                        .withLabel("iZone AC Zone " + zoneId + ": " + zone.getName()).build());
            });
        } else {
            logger.debug("iZone AC controller/zones are not available");
        }
    };
}
