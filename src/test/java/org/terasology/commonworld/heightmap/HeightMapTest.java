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
public class HeightMapTest {

    private Random r = new Random(12345);

    @Test
    public void symmetricAlongXTest() {
        Symmetry sym = Symmetries.alongX();
        basicSymmetryTest(sym);

        assertEquals(new Vector2i(123, -457), sym.getMirrored(new Vector2i(123, 456)));
    }

    @Test
    public void symmetricAlongZTest() {
        Symmetry sym = Symmetries.alongZ();
        basicSymmetryTest(sym);

        assertEquals(new Vector2i(-124, 456), sym.getMirrored(new Vector2i(123, 456)));
    }

    @Test
    public void symmetricAlongPosDiagTest() {
        Symmetry sym = Symmetries.alongPositiveDiagonal();
        basicSymmetryTest(sym);

        assertEquals(new Vector2i(100, 0), sym.getMirrored(new Vector2i(0, 100)));
        assertEquals(new Vector2i(0, 100), sym.getMirrored(new Vector2i(100, 0)));
        assertEquals(new Vector2i(10, 10), sym.getMirrored(new Vector2i(10, 10)));
    }

    @Test
    public void symmetricAlongNegDiagTest() {
        Symmetry sym = Symmetries.alongNegativeDiagonal();
        basicSymmetryTest(sym);

        assertEquals(new Vector2i(99, -1), sym.getMirrored(new Vector2i(0, -100)));
        assertEquals(new Vector2i(-1, 99), sym.getMirrored(new Vector2i(-100, 0)));
        assertEquals(new Vector2i(9, 9), sym.getMirrored(new Vector2i(-10, -10)));
    }

    private void basicSymmetryTest(Symmetry sym) {

        for (int i = 0; i < 100; i++) {
            Vector2i test = nextRandomPos();

            assertEquals(sym.isMirrored(test), sym.isMirrored(test.x, test.y));

            boolean isMirrored = sym.isMirrored(test);
            Vector2i mirrored = sym.getMirrored(test);

            assertTrue(isMirrored != sym.isMirrored(mirrored));
            assertEquals(test, sym.getMirrored(mirrored));
        }
    }

    private Vector2i nextRandomPos() {
        return new Vector2i(r.nextInt(1000) - 500, r.nextInt(1000) - 500);
    }

}
