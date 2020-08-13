/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Creation : 24 juil. 2014
 */
package org.seedstack.audit.internal;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.seedstack.audit.AuditConfig;
import org.seedstack.audit.AuditEvent;
import org.seedstack.audit.TrailExceptionHandler;
import org.seedstack.audit.spi.TrailWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuditConfigurerTest {
    private AuditConfigurer underTest;
    private AuditConfig auditConfig;
    private Map<Class<?>, Collection<Class<?>>> auditClasses;

    @Before
    public void before() {
        auditConfig = mock(AuditConfig.class);
        auditClasses = new HashMap<>();
        underTest = new AuditConfigurer(auditConfig, auditClasses);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findTrailWriters_nominal() {
        when(auditConfig.getWriters()).thenReturn(Sets.newHashSet(DummyTrailWriter.class, DummyTrailWriter2.class));
        Collection<Class<?>> trailWriterClasses = new ArrayList<>();
        trailWriterClasses.add(DummyTrailWriter.class);
        trailWriterClasses.add(DummyTrailWriter2.class);
        auditClasses.put(TrailWriter.class, trailWriterClasses);

        Set<Class<? extends TrailWriter>> result = underTest.findTrailWriters();

        assertThat(result).isNotEmpty();
        assertThat(result).containsOnly(DummyTrailWriter.class, DummyTrailWriter2.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findTrailWriters_none_defined() {
        when(auditConfig.getWriters()).thenReturn(Sets.newHashSet());
        Collection<Class<?>> trailWriterClasses = new ArrayList<>();
        trailWriterClasses.add(DummyTrailWriter.class);
        auditClasses.put(TrailWriter.class, trailWriterClasses);

        Set<Class<? extends TrailWriter>> result = underTest.findTrailWriters();

        assertThat(result).isEmpty();
    }

    @Test
    public void findTrailExceptionHandlers_nominal() {
        when(auditConfig.getExceptionHandlers()).thenReturn(Sets.newHashSet(DummyExceptionHandler.class, DummyExceptionHandler2.class));
        Collection<Class<?>> trailExceptionHandlerClasses = new ArrayList<>();
        trailExceptionHandlerClasses.add(SecurityTrailExceptionHandler.class);
        trailExceptionHandlerClasses.add(DummyExceptionHandler.class);
        trailExceptionHandlerClasses.add(DummyExceptionHandler2.class);
        auditClasses.put(TrailExceptionHandler.class, trailExceptionHandlerClasses);

        Set<Class<? extends TrailExceptionHandler<?>>> result = underTest.findTrailExceptionHandlers();

        assertThat(result).isNotEmpty();
        assertThat(result).containsOnly(DummyExceptionHandler.class, DummyExceptionHandler2.class);
    }

    @Test
    public void findTrailExceptionHandlers_no_handler_defined() {
        when(auditConfig.getExceptionHandlers()).thenReturn(Sets.newHashSet());
        Collection<Class<?>> trailExceptionHandlerClasses = new ArrayList<>();
        trailExceptionHandlerClasses.add(SecurityTrailExceptionHandler.class);
        trailExceptionHandlerClasses.add(DummyExceptionHandler.class);
        trailExceptionHandlerClasses.add(DummyExceptionHandler2.class);
        auditClasses.put(TrailExceptionHandler.class, trailExceptionHandlerClasses);

        Set<Class<? extends TrailExceptionHandler<?>>> result = underTest.findTrailExceptionHandlers();

        assertThat(result).isNotEmpty();
        assertThat(result).containsOnly(DummyExceptionHandler.class, DummyExceptionHandler2.class, SecurityTrailExceptionHandler.class);
    }

    public static class DummyTrailWriter implements TrailWriter {
        @Override
        public void writeEvent(AuditEvent auditEvent) {
        }
    }

    public static class DummyTrailWriter2 implements TrailWriter {
        @Override
        public void writeEvent(AuditEvent auditEvent) {
        }
    }

    public static class DummyExceptionHandler implements TrailExceptionHandler<Exception> {
        @Override
        public String describeException(Exception e) {
            return "dummy";
        }

    }

    public static class DummyExceptionHandler2 implements TrailExceptionHandler<Exception> {
        @Override
        public String describeException(Exception e) {
            return "dummy2";
        }

    }
}
