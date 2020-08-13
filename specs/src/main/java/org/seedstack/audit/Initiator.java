/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit;

/**
 * The initiator of an event
 */
public class Initiator {
    private final String id;
    private final String name;
    private final Address address;

    /**
     * Constructor.
     *
     * @param id   id of initiator
     * @param name the name
     * @param host the initiator hostname
     */
    public Initiator(String id, String name, String host) {
        this.id = id;
        this.name = name;
        this.address = Address.forHostName(host);
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
