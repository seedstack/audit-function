/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * The initiator of an event
 *
 * @author yves.dautremay@mpsa.com
 */
public class Initiator {

    // FIXME remove logging code from specs
    private static final Logger LOG = LoggerFactory.getLogger(Initiator.class);

    private final String id;

    private final String name;

    private Address address;

    /**
     * Constructor.
     *
     * @param id        id of initiator
     * @param name      the name
     * @param ipAddress the ip address
     */
    public Initiator(String id, String name, String ipAddress) {
        this.id = id;
        this.name = name;
        try {
            this.address = new Address(ipAddress);
        } catch (UnknownHostException e) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(e.getMessage(), e);
            }
            this.address = null;
            LOG.warn("Could not retrieve an IP address for connected user : {}", e.getMessage());
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

}
