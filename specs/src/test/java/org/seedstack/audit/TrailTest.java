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

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

public class TrailTest {

    @Test
    public void testTrail() {
        final long id = 36;
        Initiator initiator = Mockito.mock(Initiator.class);
        Host host = Mockito.mock(Host.class);
        Trail trail = new Trail(id, initiator, host);
        Assertions.assertThat(trail.getId()).isEqualTo(id);
        Assertions.assertThat(trail.getInitiator()).isEqualTo(initiator);
        Assertions.assertThat(trail.getHost()).isEqualTo(host);

        Assertions.assertThat(trail.equals(trail)).isTrue();
        Assertions.assertThat(trail.equals(null)).isFalse();

        Trail trail2 = new Trail(id, null, null);
        Assertions.assertThat(trail).isEqualTo(trail2);
        Assertions.assertThat(trail.hashCode()).isEqualTo(trail2.hashCode());

        Trail trail3 = new Trail(12, null, null);
        Assertions.assertThat(trail).isNotEqualTo(trail3);

        Assertions.assertThat(trail.equals(new Object())).isFalse();
    }
}
