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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.seedstack.audit.AuditEvent;
import org.seedstack.audit.Host;
import org.seedstack.audit.Trail;
import org.seedstack.audit.spi.TrailWriter;
import org.seedstack.seed.Application;
import org.seedstack.seed.security.SecuritySupport;
import org.seedstack.seed.security.principals.Principals;

public class DefaultAuditServiceTest {

    private DefaultAuditService underTest;

    private Application application;

    private SecuritySupport securitySupport;

    private Set<TrailWriter> trailWriters;

    @Before
    public void before() {
        underTest = new DefaultAuditService();
        application = mock(Application.class);
        securitySupport = mock(SecuritySupport.class);
        trailWriters = new HashSet<TrailWriter>();

        Whitebox.setInternalState(underTest, "application", application);
        Whitebox.setInternalState(underTest, "securitySupport", securitySupport);
        Whitebox.setInternalState(underTest, "trailWriters", trailWriters);
    }

    @Test
    public void test_initHost() {
        final String id = "id";
        final String name = "name";
        when(application.getId()).thenReturn(id);
        when(application.getName()).thenReturn(name);

        underTest.initHost();

        Host host = underTest.getHost();
        assertThat(host.getId()).isEqualTo(id);
        assertThat(host.getName()).isEqualTo(name);
    }

    @Test
    public void createTrail_nominal() {
        Whitebox.setInternalState(underTest, "host", null);
        final String id = "id";
        final String identity = "identity";
        final String fullName = "fullName";
        when(application.getId()).thenReturn(id);
        when(application.getName()).thenReturn("name");
        when(securitySupport.isAuthenticated()).thenReturn(true);
        when(securitySupport.getSimplePrincipalByName(Principals.FULL_NAME)).thenReturn(Principals.fullNamePrincipal(fullName));
        when(securitySupport.getSimplePrincipalByName(Principals.IDENTITY)).thenReturn(Principals.identityPrincipal(identity));
        when(securitySupport.getHost()).thenReturn("localhost");

        Trail t = underTest.createTrail();

        assertThat(t.getInitiator().getName()).isEqualTo(fullName);
        assertThat(t.getInitiator().getId()).isEqualTo(identity);
        assertThat(t.getId()).isGreaterThanOrEqualTo(0);
    }

    @Test
    public void createTrail_noFullName() {
        Whitebox.setInternalState(underTest, "host", null);
        final String id = "id";
        final String identity = "identity";
        when(application.getId()).thenReturn(id);
        when(application.getName()).thenReturn("name");
        when(securitySupport.isAuthenticated()).thenReturn(true);
        when(securitySupport.getSimplePrincipalByName(Principals.FULL_NAME)).thenReturn(null);
        when(securitySupport.getSimplePrincipalByName(Principals.IDENTITY)).thenReturn(Principals.identityPrincipal(identity));
        when(securitySupport.getHost()).thenReturn("localhost");

        Trail t = underTest.createTrail();

        assertThat(t.getInitiator().getName()).isEmpty();
        assertThat(t.getInitiator().getId()).isEqualTo(identity);
        assertThat(t.getId()).isGreaterThanOrEqualTo(0);
    }

    @Test
    public void createTrail_notAuthenticated() {
        Whitebox.setInternalState(underTest, "host", null);
        final String id = "id";
        when(application.getId()).thenReturn(id);
        when(application.getName()).thenReturn("name");
        when(securitySupport.isAuthenticated()).thenReturn(false);

        Trail t = underTest.createTrail();

        assertThat(t.getInitiator().getName()).isEqualTo("unknow user name");
        assertThat(t.getInitiator().getId()).isEqualTo("unknown user id");
        assertThat(t.getId()).isGreaterThanOrEqualTo(0);
    }

    @Test
    public void trail_nominal() {
        TrailWriter tw = mock(TrailWriter.class);
        trailWriters.add(tw);
        Trail trail = mock(Trail.class);

        underTest.trail("dummy", trail);

        verify(tw).writeEvent(any(AuditEvent.class));
    }
}
