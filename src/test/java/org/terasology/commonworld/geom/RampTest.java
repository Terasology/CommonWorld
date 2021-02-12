// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld.geom;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the {@link Ramp} class.
 */
public class RampTest {

    private double eps = 0.001;

    @Test
    public void simpleTest() {
        Vector3fc a = new Vector3f(10, 0, 10);
        Vector3fc b = new Vector3f(10, 1, 20);
        Ramp ramp = new Ramp(a, b);

        assertEquals(0.2, ramp.getY(10, 12), eps);
        assertEquals(0.9, ramp.getY(10, 19), eps);

        assertEquals(-0.1, ramp.getY(10, 9), eps);
        assertEquals(1.1, ramp.getY(10, 21), eps);

        assertEquals(0.0, ramp.getClampedY(10, 9), eps);
        assertEquals(1.0, ramp.getClampedY(10, 21), eps);

        assertEquals(0.3, ramp.getY(18, 13), eps);
        assertEquals(0.3, ramp.getY(79, 13), eps);

        assertEquals(1, Math.abs(ramp.getKappaNorm(9, 13)), eps);
        assertEquals(1, Math.abs(ramp.getKappaNorm(11, 13)), eps);
    }
}
