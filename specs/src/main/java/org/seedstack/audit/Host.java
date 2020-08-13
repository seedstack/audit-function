/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit;

/**
 * Represents the machine and application
 */
public class Host {
    private final String name;
    private final String id;
    private final Address address;

    /**
     * Constructor where hostname is given. If hostname is unknown, address will be null.
     *
     * @param id       functional id of the host
     * @param name     functional name of the host
     * @param hostName hostname
     */
    public Host(String id, String name, String hostName) {
        this.id = id;
        this.name = name;
        this.address = Address.forHostName(hostName);
    }

    /**
     * Constructor without hostname. Hostname will be resolved with localhost
     *
     * @param id   functional id of the host
     * @param name functional name of the host
     */
    public Host(String id, String name) {
        this.id = id;
        this.name = name;
        this.address = Address.forLocalHost();
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
