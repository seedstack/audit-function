/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Creation : 23 juil. 2014
 */
package org.seedstack.audit.internal;

import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.seedstack.audit.AuditEvent;
import org.seedstack.audit.TrailExceptionHandler;
import org.seedstack.audit.spi.TrailWriter;

import com.google.inject.Binder;

public class AuditModuleTest {

    private AuditModule underTest;

    private Set<Class<? extends TrailWriter>> trailWriters;

    private Set<Class<? extends TrailExceptionHandler>> trailExceptionHandlers;

    @Before
    public void before() {
        trailWriters = new HashSet<Class<? extends TrailWriter>>();
        trailExceptionHandlers = new HashSet<Class<? extends TrailExceptionHandler>>();

        AuditConfigurer configurer = mock(AuditConfigurer.class, RETURNS_MOCKS);
        when(configurer.findTrailWriters()).thenReturn(trailWriters);
        when(configurer.findTrailExceptionHandlers()).thenReturn(trailExceptionHandlers);

        underTest = new AuditModule(configurer);

        Binder b = mock(Binder.class, RETURNS_MOCKS);
        Whitebox.setInternalState(underTest, "binder", b);
    }

    @Test
    public void test_configure_nominal() {
        trailWriters.add(SomeWriter.class);
        trailExceptionHandlers.add(SecurityTrailExceptionHandler.class);
        underTest.configure();
    }

    public static class SomeWriter implements TrailWriter {
        @Override
        public void writeEvent(AuditEvent auditEvent) {
        }

    }
}
