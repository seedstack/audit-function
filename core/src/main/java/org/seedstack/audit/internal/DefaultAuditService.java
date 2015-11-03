/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
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
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Default implementation of AuditService.
 * 
 * @author yves.dautremay@mpsa.com
 */
public class DefaultAuditService implements AuditService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuditService.class);

    private static Host host;

    private static AtomicLong trailIds = new AtomicLong(0);

    @Inject
    private Application application;

    @Inject
    private SecuritySupport securitySupport;

    @Inject
    private Set<TrailWriter> trailWriters;

    @Override
    public Trail createTrail() {
        return new Trail(trailIds.getAndIncrement(), createInitiator(), getHost());
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
            LOG.warn("An audited code is being run by an unauthenticated user");
            initiator = new Initiator("unknown user id", "unknow user name", securitySupport.getHost());
        }
        return initiator;
    }

    void initHost() {
        host = new Host(application.getId(), application.getName());
    }

    Host getHost() {
        if (host == null) {
            initHost();
        }
        return host;
    }
}
