/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Creation : 22 juil. 2014
 */
package org.seedstack.audit;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.net.UnknownHostException;

public class AddressTest {

    @Test(expected = UnknownHostException.class)
    public void testAddress_unknownHost() throws UnknownHostException {
        new Address("dummy");
    }

    @Test
    public void testAddress_localhost() throws UnknownHostException {
        Address address = new Address("localhost");
        Assertions.assertThat(address.getIpAddress()).isEqualTo("127.0.0.1");
        Assertions.assertThat(address.getDnsName()).isEqualTo("localhost");
    }
}
