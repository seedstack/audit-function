/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Creation : 30 juin 2014
 */
package org.seedstack.business.audit;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.audit.Audited;
import org.seedstack.seed.it.SeedITRunner;
import org.seedstack.seed.it.ITBind;
import org.seedstack.seed.security.WithUser;

@RunWith(SeedITRunner.class)
public class IntegrationTest {

    @Inject
    private AuditedMethods auditedMethods;

    @Test
    @WithUser(id = "Obiwan", password = "yodarulez")
    public void testStuff() throws Exception {
        auditedMethods.audited("pouet");
    }

    @ITBind
    public static class AuditedMethods {

        @Audited(messageBefore = "AUDITING with argument ${args[0]}........", messageAfter = "AUDITED !!!!! !${result}")
        public String audited(String someString) {
            return "this is returned";
        }
    }
}
