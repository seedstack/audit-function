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

public class HostTest {
    final String id = "id";
    final String name = "name";

    @Test
    public void hostTest_unknownHost() {
        Host host = new Host(id, name, "dummy");

        Assertions.assertThat(host.getId()).isEqualTo(id);
        Assertions.assertThat(host.getName()).isEqualTo(name);
        Assertions.assertThat(host.getAddress().getDnsName()).isEqualTo("unknown");
        Assertions.assertThat(host.getAddress().getIpAddress()).isEqualTo("unknown");
    }

    @Test
    public void hostTest_knownHost() {
        Host host = new Host(id, name, "localhost");

        assertHost(host);
    }

    @Test
    public void hostTest_localhost() {
        Host host = new Host(id, name);

        assertHost(host);
    }

    private void assertHost(Host host) {
        Assertions.assertThat(host.getId()).isEqualTo(id);
        Assertions.assertThat(host.getName()).isEqualTo(name);
        Assertions.assertThat(host.getAddress()).isNotNull();
    }

}
