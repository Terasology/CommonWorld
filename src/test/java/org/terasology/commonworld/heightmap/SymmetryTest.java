/*
 * Copyright 2014 MovingBlocks
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

package org.terasology.commonworld.heightmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;


import org.joml.Vector2i;
import org.junit.Test;
import org.terasology.commonworld.symmetry.Symmetries;
import org.terasology.commonworld.symmetry.Symmetry;

/**
 * Some tests on {@link HeightMap}s
 */
public class SymmetryTest {

    private Random r = new Random(12345);

    @Test
    public void symmetricAlongXTest() {
        Symmetry shm = Symmetries.alongX();
        basicSymmetryTest(shm);

        assertEquals(new Vector2i(133, 0), shm.getMirrored(new Vector2i(133, -1)));
        assertEquals(new Vector2i(123, 456), shm.getMirrored(new Vector2i(123, -457)));
    }

    @Test
    public void symmetricAlongZTest() {
        Symmetry shm = Symmetries.alongZ();
        basicSymmetryTest(shm);

        assertEquals(new Vector2i(0, 133), shm.getMirrored(new Vector2i(-1, 133)));
        assertEquals(new Vector2i(123, 456), shm.getMirrored(new Vector2i(-124, 456)));
    }

    @Test
    public void symmetricAlongPosDiagTest() {
        Symmetry shm = Symmetries.alongPositiveDiagonal();
        basicSymmetryTest(shm);

        assertEquals(new Vector2i(0, 0), shm.getMirrored(new Vector2i(0, 0)));
        assertEquals(new Vector2i(100, 0), shm.getMirrored(new Vector2i(0, 100)));
        assertEquals(new Vector2i(0, 100), shm.getMirrored(new Vector2i(100, 0)));
        assertEquals(new Vector2i(10, 10), shm.getMirrored(new Vector2i(10, 10)));
        assertEquals(new Vector2i(-10, -10), shm.getMirrored(new Vector2i(-10, -10)));
        assertEquals(new Vector2i(0, -100), shm.getMirrored(new Vector2i(-100, 0)));
    }

    @Test
    public void symmetricAlongNegDiagTest() {
        Symmetry shm = Symmetries.alongNegativeDiagonal();
        basicSymmetryTest(shm);

        assertEquals(new Vector2i(0, 0), shm.getMirrored(new Vector2i(-1, -1)));
        assertEquals(new Vector2i(100, 0), shm.getMirrored(new Vector2i(-1, -101)));
        assertEquals(new Vector2i(0, 100), shm.getMirrored(new Vector2i(-101, -1)));
        assertEquals(new Vector2i(10, 10), shm.getMirrored(new Vector2i(-11, -11)));
    }

    private void basicSymmetryTest(Symmetry shm) {

        for (int i = 0; i < 100; i++) {
            Vector2i test = nextRandomPos();

            assertEquals(shm.isMirrored(test), shm.isMirrored(test.x, test.y));

            boolean isMirrored = shm.isMirrored(test);
            Vector2i mirrored = shm.getMirrored(test);

            assertTrue(isMirrored != shm.isMirrored(mirrored));
            assertEquals(test, shm.getMirrored(mirrored));
        }
    }

    private Vector2i nextRandomPos() {
        return new Vector2i(r.nextInt(1000) - 500, r.nextInt(1000) - 500);
    }

}
