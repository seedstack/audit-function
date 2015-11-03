/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit.internal;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.seedstack.audit.AuditService;
import org.seedstack.audit.Audited;
import org.seedstack.audit.TrailExceptionHandler;
import org.seedstack.audit.spi.TrailWriter;

import java.util.Set;

/**
 * Module for the audit
 * 
 * @author yves.dautremay@mpsa.com
 */
public class AuditModule extends AbstractModule {

    private AuditConfigurer auditConfigurer;

    /**
     * Constructor
     * 
     * @param auditConfigurer the audit configurator
     */
    public AuditModule(AuditConfigurer auditConfigurer) {
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

        // Following lines allow to call an init method on the service
        final InjectionListener injectionListener = new InjectionListener<DefaultAuditService>() {
            @Override
            public void afterInjection(DefaultAuditService injectee) {
                injectee.initHost();
            }
        };
        TypeListener typeListener = new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                encounter.register(injectionListener);
            }
        };
        bindListener(new AbstractMatcher<TypeLiteral<?>>() {
            @Override
            public boolean matches(TypeLiteral<?> typeLiteral) {
                return DefaultAuditService.class.isAssignableFrom(typeLiteral.getRawType());
            }
        }, typeListener);
    }

}
