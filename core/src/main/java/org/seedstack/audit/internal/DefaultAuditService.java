/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit.internal;

import org.seedstack.audit.AuditEvent;
import org.seedstack.audit.AuditService;
import org.seedstack.audit.Host;
import org.seedstack.audit.Initiator;
import org.seedstack.audit.Trail;
import org.seedstack.audit.spi.TrailWriter;
import org.seedstack.seed.Application;
import org.seedstack.seed.security.SecuritySupport;
import org.seedstack.seed.security.principals.Principals;
import org.seedstack.seed.security.principals.SimplePrincipalProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

class DefaultAuditService implements AuditService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAuditService.class);
    private final static AtomicLong trailIds = new AtomicLong(0);
    private volatile static Host trailHost;
    private final SecuritySupport securitySupport;
    private final Set<TrailWriter> trailWriters;

    @Inject
    public DefaultAuditService(Application application, SecuritySupport securitySupport, Set<TrailWriter> trailWriters) {
        this.securitySupport = securitySupport;
        this.trailWriters = new HashSet<>(trailWriters);
        if (trailHost == null) {
            synchronized (DefaultAuditService.class) {
                if (trailHost == null) {
                    trailHost = new Host(application.getId(), application.getName());
                }
            }
        }
    }

    @Override
    public Trail createTrail() {
        return new Trail(trailIds.getAndIncrement(), createInitiator(), trailHost);
    }

    @Override
    public void trail(String message, Trail trail) {
        AuditEvent auditEvent = new AuditEvent(message, trail);
        for (TrailWriter writer : trailWriters) {
            writer.writeEvent(auditEvent);
        }
    }

    private Initiator createInitiator() {
        Initiator initiator;
        if (securitySupport.isAuthenticated()) {
            SimplePrincipalProvider fullNamePrincipal = securitySupport.getSimplePrincipalByName(Principals.FULL_NAME);
            String fullName;
            if (fullNamePrincipal == null) {
                fullName = "";
            } else {
                fullName = fullNamePrincipal.getValue();
            }
            initiator = new Initiator(securitySupport.getSimplePrincipalByName(Principals.IDENTITY).getValue(), fullName, securitySupport.getHost());
        } else {
            LOGGER.warn("An audited code is being run by an unauthenticated user");
            initiator = new Initiator("<unknown user id>", "<unknown user name>", securitySupport.getHost());
        }
        return initiator;
    }
}
