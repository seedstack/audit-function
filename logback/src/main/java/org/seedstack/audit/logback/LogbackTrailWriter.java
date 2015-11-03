/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit.logback;

import org.seedstack.audit.AuditEvent;
import org.seedstack.audit.spi.TrailWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TrailWriter implementation for Logback.
 */
public class LogbackTrailWriter implements TrailWriter {

    private Logger auditLogger = LoggerFactory.getLogger("AUDIT_LOGGER");

    @Override
    public void writeEvent(AuditEvent auditEvent) {
        auditLogger.info("", auditEvent);
    }

}
