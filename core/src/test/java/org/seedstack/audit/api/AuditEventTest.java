/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Creation : 23 juil. 2014
 */
package org.seedstack.audit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.seedstack.audit.AuditEvent;
import org.seedstack.audit.Trail;

public class AuditEventTest {

    @Test
    public void testAuditEvent() {
        final String message = "message";
        Trail trail = mock(Trail.class);
        AuditEvent underTest = new AuditEvent(message, trail);
        assertThat(underTest.getMessage()).isEqualTo(message);
        assertThat(underTest.getTrail()).isEqualTo(trail);

        assertThat(underTest.getDate()).isNotNull();
        assertThat(underTest.getFormattedDate("y")).isNotNull();
    }
}
