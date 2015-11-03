/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit.internal;

import org.seedstack.audit.TrailExceptionHandler;
import org.seedstack.seed.security.AuthorizationException;

/**
 * The implementation of trail exception handler for the security.
 */
public class SecurityTrailExceptionHandler implements TrailExceptionHandler<AuthorizationException> {

    @Override
    public String describeException(AuthorizationException e) {
        return "Authorization exception: user does not have sufficient rights";
    }
}
