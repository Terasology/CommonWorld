// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link UnorderedPair} class.
 */
public class UnorderedPairTest {
    @Test
    public void equalsTest() {
        Assert.assertEquals(new UnorderedPair<>(0, 0), new UnorderedPair<>(0, 0));
        Assert.assertEquals(new UnorderedPair<>(1, 1), new UnorderedPair<>(1, 1));
        Assert.assertEquals(new UnorderedPair<>(1, 13), new UnorderedPair<>(1, 13));
        Assert.assertEquals(new UnorderedPair<>(1, 13), new UnorderedPair<>(13, 1));
    }

    @Test
    public void notEqualsTest() {
        Assert.assertNotEquals(new UnorderedPair<>(1, 1), new UnorderedPair<>(0, 1));
        Assert.assertNotEquals(new UnorderedPair<>(0, 13), new UnorderedPair<>(0, 1));
        Assert.assertNotEquals(new UnorderedPair<>(0, 13), new Object());
        Assert.assertNotEquals(new UnorderedPair<>(0, 13), null);
    }
}
