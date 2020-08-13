/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit.logback;

import org.seedstack.shed.reflect.ReflectUtils;

import java.lang.reflect.Field;

public final class TestUtils {
    private TestUtils() {
        // no instantiation
    }

    static void setField(Object instance, String fieldName, Object value) {
        Field field = null;
        Class<?> clazz = instance.getClass();
        do {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException noSuchFieldException) {
                // look up
                clazz = clazz.getSuperclass();
            }
        } while (!clazz.equals(Object.class));

        if (field == null) {
            throw new IllegalStateException("Cannot find field " + fieldName + " of class " + instance.getClass().getName());
        }
        ReflectUtils.setValue(ReflectUtils.makeAccessible(field), instance, value);
    }
}
