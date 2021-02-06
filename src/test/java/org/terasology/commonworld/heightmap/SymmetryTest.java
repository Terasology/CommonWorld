// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld.heightmap;

import org.joml.Vector2i;
import org.junit.Test;
import org.terasology.commonworld.symmetry.Symmetries;
import org.terasology.commonworld.symmetry.Symmetry;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
