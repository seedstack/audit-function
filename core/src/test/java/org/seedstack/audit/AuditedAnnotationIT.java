/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Creation : 30 juin 2014
 */
package org.seedstack.audit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.seed.Bind;
import org.seedstack.seed.security.AuthorizationException;
import org.seedstack.seed.security.WithUser;
import org.seedstack.seed.testing.junit4.SeedITRunner;

import javax.inject.Inject;

@RunWith(SeedITRunner.class)
public class AuditedAnnotationIT {

    @Inject
    private AuditedMethods auditedMethods;

    @Test(expected = AuthorizationException.class)
    @WithUser(id = "Obiwan", password = "yodarulez")
    public void testStuff() throws Exception {
        auditedMethods.audited("pouet");
        auditedMethods.auditedWithException();
    }

    @Bind
    public static class AuditedMethods {

        @Audited(messageBefore = "AUDITING with argument ${args[0]}........", messageAfter = "AUDITED !!!!! !${result}")
        public String audited(String someString) {
            reaudited();
            return "this is returned";
        }

        @Audited(messageBefore = "REAUDITING...", messageAfter = "REAUDITED !!!!!! ")
        public void reaudited() {

        }

        @Audited(messageBefore = "AUDITING EXCEPTION...", messageAfter = "AUDITED !!!!!!", messageOnException = "OMG I Crashed !! ${exception.getMessage()}")
        public void auditedWithException() throws Exception {
            throw new AuthorizationException("what a crash");
        }
    }
}
