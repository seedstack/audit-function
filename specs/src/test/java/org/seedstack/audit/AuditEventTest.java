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
package org.seedstack.audit;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

public class AuditEventTest {

    @Test
    public void testAuditEvent() {
        final String message = "message";
        Trail trail = Mockito.mock(Trail.class);
        AuditEvent underTest = new AuditEvent(message, trail);
        Assertions.assertThat(underTest.getMessage()).isEqualTo(message);
        Assertions.assertThat(underTest.getTrail()).isEqualTo(trail);

        Assertions.assertThat(underTest.getDate()).isNotNull();
        Assertions.assertThat(underTest.getFormattedDate("y")).isNotNull();
    }
}
