/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Creation : 23 juil. 2014
 */
package org.seedstack.audit.internal;

import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import org.apache.commons.configuration.Configuration;
import org.junit.Test;
import org.mockito.Mockito;
import org.seedstack.seed.core.spi.configuration.ConfigurationProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuditPluginTest {

    @Test
    public void testPlugin() {
        AuditPlugin underTest = new AuditPlugin();

        assertThat(underTest.name()).isNotNull();
        assertThat(underTest.classpathScanRequests()).isNotNull();
        assertThat(underTest.nativeUnitModule()).isNotNull();
        assertThat(underTest.requiredPlugins()).isNotNull();

        InitContext iniContext = mock(InitContext.class, Mockito.RETURNS_MOCKS);
        ConfigurationProvider configurationProvider = mock(ConfigurationProvider.class);
        when(configurationProvider.getConfiguration()).thenReturn(mock(Configuration.class));
        when(iniContext.dependency(ConfigurationProvider.class)).thenReturn(configurationProvider);
        InitState state = underTest.init(iniContext);
        assertThat(state).isEqualTo(InitState.INITIALIZED);
    }
}
