/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Creation : 23 juil. 2014
 */
package org.seedstack.audit.internal;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.seedstack.audit.AuditEvent;
import org.seedstack.audit.Trail;
import org.seedstack.audit.spi.TrailWriter;
import org.seedstack.seed.Application;
import org.seedstack.seed.security.SecuritySupport;
import org.seedstack.seed.security.principals.Principals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultAuditServiceTest {
    private DefaultAuditService underTest;
    private Application application;
    private SecuritySupport securitySupport;
    private TrailWriter trailWriter;

    @Before
    public void before() {
        application = mock(Application.class);
        securitySupport = mock(SecuritySupport.class);
        trailWriter = mock(TrailWriter.class);
        underTest = new DefaultAuditService(application, securitySupport, Sets.newHashSet(trailWriter));

    }

    @Test
    public void createTrail_nominal() {
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
        final String id = "id";
        when(application.getId()).thenReturn(id);
        when(application.getName()).thenReturn("name");
        when(securitySupport.isAuthenticated()).thenReturn(false);

        Trail t = underTest.createTrail();

        assertThat(t.getInitiator().getName()).isEqualTo("<unknown user name>");
        assertThat(t.getInitiator().getId()).isEqualTo("<unknown user id>");
        assertThat(t.getId()).isGreaterThanOrEqualTo(0);
    }

    @Test
    public void trail_nominal() {
        Trail trail = mock(Trail.class);
        underTest.trail("dummy", trail);
        verify(trailWriter).writeEvent(any(AuditEvent.class));
    }
}
