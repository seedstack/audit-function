/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
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
 *
 * @author yves.dautremay@mpsa.com
 */
public class Address {

    private final String dnsName;

    private final String ipAddress;

    /**
     * Contructor with a hostname
     *
     * @param hostName the hostname
     * @throws UnknownHostException if the host is unknown
     */
    public Address(String hostName) throws UnknownHostException {
        this(InetAddress.getByName(hostName));
    }

    /**
     * Constructor with an INETAddress
     *
     * @param address th address
     */
    public Address(InetAddress address) {
        this.ipAddress = address.getHostAddress();
        this.dnsName = address.getHostName();
    }

    public String getDnsName() {
        return dnsName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

}
