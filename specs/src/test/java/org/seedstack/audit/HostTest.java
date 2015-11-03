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

public class HostTest {

    @Test
    public void hostTest_unknownHost() {
        final String id = "id";
        final String name = "name";
        Host host = new Host(id, name, "dummy");
        Assertions.assertThat(host.getId()).isEqualTo(id);
        Assertions.assertThat(host.getName()).isEqualTo(name);
        Assertions.assertThat(host.getAddress()).isNull();
    }

    @Test
    public void hostTest_knownHost() {
        final String id = "id";
        final String name = "name";
        Host host = new Host(id, name, "localhost");
        Assertions.assertThat(host.getId()).isEqualTo(id);
        Assertions.assertThat(host.getName()).isEqualTo(name);
        Assertions.assertThat(host.getAddress()).isNotNull();
    }

    @Test
    public void hostTest_localhost() {
        final String id = "id";
        final String name = "name";
        Host host = new Host(id, name);
        Assertions.assertThat(host.getId()).isEqualTo(id);
        Assertions.assertThat(host.getName()).isEqualTo(name);
        Assertions.assertThat(host.getAddress()).isNotNull();
    }
}
