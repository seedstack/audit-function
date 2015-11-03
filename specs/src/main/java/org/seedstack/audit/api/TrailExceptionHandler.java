/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.audit;

/**
 * Allows to describe an event in case of a certain exception.
 *
 * @param <E> exception handled by the handler
 * @author yves.dautremay@mpsa.com
 */
public interface TrailExceptionHandler<E extends Exception> {

    /**
     * Creates a message for an exception.
     *
     * @param e the exception to describe
     * @return the description
     */
    String describeException(E e);
}
