/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit.internal;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import org.seedstack.audit.AuditService;
import org.seedstack.audit.Audited;
import org.seedstack.audit.TrailExceptionHandler;
import org.seedstack.audit.spi.TrailWriter;

import java.util.Set;

class AuditModule extends AbstractModule {
    private final AuditConfigurer auditConfigurer;

    AuditModule(AuditConfigurer auditConfigurer) {
        this.auditConfigurer = auditConfigurer;
    }

    @Override
    protected void configure() {
        bind(AuditService.class).to(DefaultAuditService.class);
        Multibinder<TrailWriter> writers = Multibinder.newSetBinder(binder(), TrailWriter.class);
        Set<Class<? extends TrailWriter>> foundTrailWriters = auditConfigurer.findTrailWriters();
        for (Class<? extends TrailWriter> trailWriterClass : foundTrailWriters) {
            writers.addBinding().to(trailWriterClass);
        }

        Multibinder<TrailExceptionHandler> exHandlers = Multibinder.newSetBinder(binder(), TrailExceptionHandler.class);
        for (Class<? extends TrailExceptionHandler> exceptionHandlerClass : auditConfigurer.findTrailExceptionHandlers()) {
            exHandlers.addBinding().to(exceptionHandlerClass);
        }

        AuditedInterceptor interceptor = new AuditedInterceptor();
        requestInjection(interceptor);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Audited.class), interceptor);
    }
}
