/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
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

public class InitiatorTest {

    @Test
    public void testInitiator_localhost() {
        final String id = "id";
        final String name = "name";
        String ipAddress = "127.0.0.1";
        Initiator initiator = new Initiator(id, name, ipAddress);
        Assertions.assertThat(initiator.getId()).isEqualTo(id);
        Assertions.assertThat(initiator.getName()).isEqualTo(name);
        Assertions.assertThat(initiator.getAddress()).isNotNull();
    }

    @Test
    public void testInitiator_unknownHost() {
        final String id = "id";
        final String name = "name";
        String ipAddress = "dummy";
        Initiator initiator = new Initiator(id, name, ipAddress);
        Assertions.assertThat(initiator.getId()).isEqualTo(id);
        Assertions.assertThat(initiator.getName()).isEqualTo(name);
        Assertions.assertThat(initiator.getAddress().getDnsName()).isEqualTo("unknown");
        Assertions.assertThat(initiator.getAddress().getIpAddress()).isEqualTo("unknown");
    }
}
