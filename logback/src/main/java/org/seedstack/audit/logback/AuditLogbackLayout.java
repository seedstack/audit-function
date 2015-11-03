/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit.logback;

import javax.el.ELContext;
import javax.inject.Inject;

import org.seedstack.audit.AuditEvent;
import org.seedstack.seed.Configuration;
import org.seedstack.seed.el.ELContextBuilder;
import org.seedstack.seed.el.ELService;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

/**
 * LogbackLayout for trace of audit trails.
 * 
 * @author yves.dautremay@mpsa.com
 */
public class AuditLogbackLayout extends LayoutBase<ILoggingEvent> {

    @Inject
    private ELService elService;

    @Inject
    private ELContextBuilder elContextBuilder;

    @Configuration("org.seedstack.business.audit.logPattern")
    private String elExpression;

    @Override
    public String doLayout(ILoggingEvent loggingEvent) {
        AuditEvent auditEvent = (AuditEvent) loggingEvent.getArgumentArray()[0];
        ELContext elContext = elContextBuilder.defaultContext().withProperty("event", auditEvent).withProperty("trail", auditEvent.getTrail())
                .withProperty("initiator", auditEvent.getTrail().getInitiator()).withProperty("host", auditEvent.getTrail().getHost()).build();
        return elService.withExpression(elExpression, String.class).withContext(elContext).asValueExpression().eval() + CoreConstants.LINE_SEPARATOR;
    }
}