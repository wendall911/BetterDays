/*
 * Derived from Hourglass
 * https://github.com/DuckyCrayfish/hourglass
 * Copyright (C) 2021 Nick Iacullo
 *
 * This file is part of Better Days.
 *
 * Better Days is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Better Days is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Better Days.  If not, see <https://www.gnu.org/licenses/>.
 */

package betterdays.wrappers;

/** A class wrapper used to reduce the number of references to external classes in this codebase. */
public class Wrapper<T> {

    /** The wrapped object. */
    protected final T wrapped;

    /**
     * Instantiates a new object wrapper.
     * @param object  the object to wrap
     */
    public Wrapper(T object) {
        this.wrapped = object;
    }

    /** {@return the wrapped object} */
    public T get() {
        return wrapped;
    }

}
