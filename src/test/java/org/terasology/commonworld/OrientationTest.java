// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld;

import org.junit.jupiter.api.Test;

import static org.terasology.commonworld.Orientation.EAST;
import static org.terasology.commonworld.Orientation.NORTH;
import static org.terasology.commonworld.Orientation.NORTHEAST;
import static org.terasology.commonworld.Orientation.NORTHWEST;
import static org.terasology.commonworld.Orientation.SOUTH;
import static org.terasology.commonworld.Orientation.SOUTHEAST;
import static org.terasology.commonworld.Orientation.SOUTHWEST;
import static org.terasology.commonworld.Orientation.WEST;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 * Tests {@link Orientation}
 */
public class OrientationTest {

    @Test
    public void testOpposites() {
        assertEquals(SOUTH, NORTH.getOpposite());
        assertEquals(NORTH, SOUTH.getOpposite());
        assertEquals(EAST, WEST.getOpposite());
        assertEquals(WEST, EAST.getOpposite());
        assertEquals(NORTHEAST, SOUTHWEST.getOpposite());
        assertEquals(SOUTHWEST, NORTHEAST.getOpposite());
        assertEquals(NORTHWEST, SOUTHEAST.getOpposite());
        assertEquals(SOUTHEAST, NORTHWEST.getOpposite());
    }

    @Test
    public void testInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            NORTH.getRotated(12);
        });
    }

    @Test
    public void testRotationClockwise() {
        assertEquals(WEST, SOUTH.getRotated(90));
        assertEquals(SOUTH, EAST.getRotated(90));
        assertEquals(EAST, NORTH.getRotated(90));
        assertEquals(NORTH, WEST.getRotated(90));
    }

    @Test
    public void testRotationCounterClockwise() {
        assertEquals(SOUTH, WEST.getRotated(-90));
        assertEquals(EAST, SOUTH.getRotated(-90));
        assertEquals(NORTH, EAST.getRotated(-90));
        assertEquals(WEST, NORTH.getRotated(-90));
    }

    @Test
    public void testRotationModulo() {
        assertEquals(NORTH, SOUTH.getRotated(180));
        assertEquals(NORTH, SOUTH.getRotated(720 + 180));
        assertEquals(NORTH, SOUTH.getRotated(-180));
        assertEquals(NORTH, SOUTH.getRotated(-720 - 180));

        assertEquals(NORTH, NORTH.getRotated(0));
        assertEquals(NORTH, NORTH.getRotated(360));
        assertEquals(NORTH, NORTH.getRotated(3600));
        assertEquals(NORTH, NORTH.getRotated(-3600));
    }
}
