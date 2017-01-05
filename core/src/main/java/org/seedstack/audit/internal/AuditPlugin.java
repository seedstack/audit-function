/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit.internal;

import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import org.seedstack.audit.AuditConfig;
import org.seedstack.audit.TrailExceptionHandler;
import org.seedstack.audit.spi.TrailWriter;
import org.seedstack.seed.core.internal.AbstractSeedPlugin;

import java.util.Collection;

/**
 * Plugin for auditing an application's functional behavior.
 */
public class AuditPlugin extends AbstractSeedPlugin {
    private AuditConfigurer auditConfigurer;

    @Override
    public String name() {
        return "audit";
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {
        return classpathScanRequestBuilder().descendentTypeOf(TrailWriter.class).descendentTypeOf(TrailExceptionHandler.class).build();
    }

    @Override
    public InitState initialize(InitContext initContext) {
        auditConfigurer = new AuditConfigurer(getConfiguration(AuditConfig.class), initContext.scannedSubTypesByAncestorClass());
        return InitState.INITIALIZED;
    }

    @Override
    public Object nativeUnitModule() {
        return new AuditModule(auditConfigurer);
    }
}
