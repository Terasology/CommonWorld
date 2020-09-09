// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld;

import com.google.common.base.Preconditions;

/**
 * Defines an unordered tuple of two objects of the same type. The methods equals() and hashCode() are implemented so
 * that the pair (a, b) and (b, a) have the same hash code and equals() returns true.
 *
 * @param <T> the element type
 */
public final class UnorderedPair<T> {

    private final T a;
    private final T b;

    /**
     * Constructs a pair based on two instances.
     *
     * @param a one element, never <code>null</code>.
     * @param b another element, never <code>null</code>.
     */
    public UnorderedPair(T a, T b) {
        Preconditions.checkArgument(a != null && b != null, "argument must not be null");

        this.a = a;
        this.b = b;
    }

    /**
     * @return one element of the pair
     */
    public T getA() {
        return this.a;
    }

    /**
     * @return the other element of the pair
     */
    public T getB() {
        return this.b;
    }

    @Override
    public int hashCode() {
        // the hash code must be equal for [a, b] and [b, a]
        // perform xor with a large prime number to avoid degenerate hash codes
        // Otherwise, if one of the two hash codes was zero the result would be zero, too
        return (a.hashCode() ^ 1262887) * (b.hashCode() ^ 1262887);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (obj.getClass() != getClass()) {
            return false;
        }

        UnorderedPair<?> that = (UnorderedPair<?>) obj;

        return (a.equals(that.a) && b.equals(that.b))
                || (a.equals(that.b) && b.equals(that.a));
    }

    @Override
    public String toString() {
        return "UnorderedPair [" + a + ", " + b + "]";
    }


}
