/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit;

import org.seedstack.audit.spi.TrailWriter;
import org.seedstack.coffig.Config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Config("audit")
public class AuditConfig {
    private Set<Class<? extends TrailWriter>> writers = new HashSet<>();
    private Set<Class<? extends TrailExceptionHandler<?>>> exceptionHandlers = new HashSet<>();

    public Set<Class<? extends TrailWriter>> getWriters() {
        return Collections.unmodifiableSet(writers);
    }

    public AuditConfig addWriter(Class<? extends TrailWriter> trailWriterClass) {
        this.writers.add(trailWriterClass);
        return this;
    }

    public Set<Class<? extends TrailExceptionHandler<?>>> getExceptionHandlers() {
        return Collections.unmodifiableSet(exceptionHandlers);
    }

    public AuditConfig addExceptionHandler(Class<? extends TrailExceptionHandler<?>> exceptionHandler) {
        this.exceptionHandlers.add(exceptionHandler);
        return this;
    }
}
