/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.commonworld.symmetry;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.terasology.math.Vector2i;

/**
 * Tests the {@link SpiralIterable} class.
 * @author Martin Steiger
 */
public final class SpiralIterableTest {

    private final List<Vector2i> expected = Arrays.asList(
            new Vector2i(3, 1), new Vector2i(4, 1), new Vector2i(4, 2),
            new Vector2i(3, 2), new Vector2i(2, 2), new Vector2i(2, 1),
            new Vector2i(2, 0), new Vector2i(3, 0), new Vector2i(4, 0),
            new Vector2i(5, 0));

    @Test
    public void testDefault() {
        SpiralIterable spiral = new SpiralIterable(new Vector2i(3, 1));

        Iterator<Vector2i> iterator = spiral.iterator();

        for (Vector2i pt : expected) {
            Assert.assertEquals(pt, iterator.next());
        }
    }
}
