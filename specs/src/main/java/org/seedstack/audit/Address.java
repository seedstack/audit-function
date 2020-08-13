/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * An internet address : an ip and/or a DNS name
 */
public class Address {
    private final String dnsName;
    private final String ipAddress;

    private Address(String dnsName, String ipAddress) {
        this.dnsName = dnsName;
        this.ipAddress = ipAddress;
    }

    public String getDnsName() {
        return dnsName;
    }

    public String getIpAddress() {
        return ipAddress;
    }


    public static Address forLocalHost() {
        try {
            return forInetAddress(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            return getUnknownAddress();
        }
    }

    public static Address forHostName(String hostName) {
        try {
            return forInetAddress(InetAddress.getByName(hostName));
        } catch (UnknownHostException e) {
            return getUnknownAddress();
        }
    }

    public static Address forInetAddress(InetAddress inetAddress) {
        return new Address(inetAddress.getHostName(), inetAddress.getHostAddress());

    }

    private static Address getUnknownAddress() {
        return new Address("unknown", "unknown");
    }
}
