/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Creation : 22 juil. 2014
 */
package org.seedstack.audit;


import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.assertj.core.api.Assertions.assertThat;

public class AddressTest {
    @Test
    public void testAddress_unknownHost() {
        assertThat(Address.forHostName("dummy").getDnsName()).isEqualTo("unknown");
        assertThat(Address.forHostName("dummy").getIpAddress()).isEqualTo("unknown");
    }

    @Test
    public void testAddress_localhost() throws UnknownHostException {
        Address address = Address.forLocalHost();
        assertThat(address.getDnsName()).isEqualTo(InetAddress.getLocalHost().getHostName());
        assertThat(address.getIpAddress()).isEqualTo(InetAddress.getLocalHost().getHostAddress());
    }
}
