/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit.internal;

import org.seedstack.audit.AuditConfig;
import org.seedstack.audit.TrailExceptionHandler;
import org.seedstack.audit.spi.TrailWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Reads the configuration to deduce the classes to use
 */
class AuditConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditConfigurer.class);
    private final AuditConfig auditConfig;
    private final Map<Class<?>, Collection<Class<?>>> auditClasses;

    /**
     * Constructor
     *
     * @param auditConfig  configuration for audit
     * @param auditClasses scanned classes
     */
    AuditConfigurer(AuditConfig auditConfig, Map<Class<?>, Collection<Class<?>>> auditClasses) {
        this.auditConfig = auditConfig;
        this.auditClasses = auditClasses;
    }

    /**
     * Finds the trail writers as configured in the props.
     *
     * @return a collection of trail writer classes.
     */
    Set<Class<? extends TrailWriter>> findTrailWriters() {
        Set<Class<? extends TrailWriter>> trailWriters = auditConfig.getWriters();
        if (trailWriters.isEmpty()) {
            LOGGER.info("No TrailWriter specified");
        }
        return trailWriters;
    }

    /**
     * Finds the exception handlers as configured in the props.
     *
     * @return a collection of exception handler classes.
     */
    @SuppressWarnings({"unchecked"})
    Set<Class<? extends TrailExceptionHandler<?>>> findTrailExceptionHandlers() {
        Set<Class<? extends TrailExceptionHandler<?>>> trailExceptionHandlers = auditConfig.getExceptionHandlers();
        if (trailExceptionHandlers.isEmpty()) {
            Collection<Class<?>> scannedTrailExceptionHandlers = auditClasses.get(TrailExceptionHandler.class);
            LOGGER.info("No audit TrailExceptionHandler specified, using every handler found");
            Set<Class<? extends TrailExceptionHandler<?>>> foundExceptionHandlers = new HashSet<>();
            for (Class<?> scannedTrailExceptionHandler : scannedTrailExceptionHandlers) {
                foundExceptionHandlers.add((Class<? extends TrailExceptionHandler<?>>) scannedTrailExceptionHandler);
                LOGGER.info("Registered audit exception handler {}", scannedTrailExceptionHandler);
            }
            return foundExceptionHandlers;
        } else {
            return trailExceptionHandlers;
        }
    }
}
