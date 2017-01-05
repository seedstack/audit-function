/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit.internal;

import java.util.HashSet;
import java.util.Set;

import org.seedstack.audit.Trail;

/**
 * Local thread context for audit interceptor
 */
class AuditContext {

    private Trail trail;

    private Set<Exception> auditedExceptions = new HashSet<>();

    /** Nested audited sections */
    int nbNestedAudits;

    Trail getTrail() {
        return trail;
    }

    void setTrail(Trail trail) {
        this.trail = trail;
    }

    Set<Exception> getAuditedExceptions() {
        return auditedExceptions;
    }
}
