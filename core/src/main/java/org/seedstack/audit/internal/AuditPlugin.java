/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit.internal;

import com.google.common.collect.Lists;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.core.AbstractPlugin;
import org.apache.commons.configuration.Configuration;
import org.seedstack.audit.TrailExceptionHandler;
import org.seedstack.audit.spi.TrailWriter;
import org.seedstack.seed.core.spi.configuration.ConfigurationProvider;

import java.util.Collection;

/**
 * Plugin for auditing an application's functional behavior.
 * 
 * @author yves.dautremay@mpsa.com
 */
public class AuditPlugin extends AbstractPlugin {

    private AuditConfigurer auditConfigurer;

    public static final String PROPERTIES_PREFIX = "org.seedstack.audit";

    @Override
    public String name() {
        return "audit";
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {
        return classpathScanRequestBuilder().descendentTypeOf(TrailWriter.class).descendentTypeOf(TrailExceptionHandler.class).build();
    }

    @Override
    public InitState init(InitContext initContext) {
        Configuration auditConfig = initContext.dependency(ConfigurationProvider.class).getConfiguration().subset(PROPERTIES_PREFIX);;
        auditConfigurer = new AuditConfigurer(auditConfig, initContext.scannedSubTypesByAncestorClass());
        return InitState.INITIALIZED;
    }

    @Override
    public Object nativeUnitModule() {
        return new AuditModule(auditConfigurer);
    }

    @Override
    public Collection<Class<?>> requiredPlugins() {
        return Lists.<Class<?>>newArrayList(ConfigurationProvider.class);
    }

}
