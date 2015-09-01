/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.commonworld;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link UnorderedPair} class.
 */
public class UnorderedPairTest {
    @Test
    public void equalsTest()  {
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
