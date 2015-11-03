/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit.logback.internal;


import io.nuun.kernel.core.AbstractPlugin;

/**
 * Audit logback plugin.
 */
public class AuditLogbackPlugin extends AbstractPlugin {

    @Override
    public String name() {
        return "business-audit-logback";
    }

    @Override
    public Object nativeUnitModule() {
        return new AuditLogbackModule();
    }
}
