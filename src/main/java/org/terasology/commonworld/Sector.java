// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld;

import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.ImmutableVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2i;

/**
 * Defines a square-shaped terrain sector
 */
public final class Sector {

    /**
     * Measured in blocks
     */
    public static final int SIZE = 1024;

    public static final int SIZE_X = 1024;
    public static final int SIZE_Z = 1024;

    private final ImmutableVector2i coords;

    private final Rect2i bounds;


    /**
     * @param coords the coordinates
     */
    Sector(BaseVector2i coords) {
        if (coords == null) {
            throw new NullPointerException("coords cannot be null");
        }

        this.coords = ImmutableVector2i.createOrUse(coords);
        this.bounds = Rect2i.createFromMinAndSize(coords.getX() * SIZE_X, coords.getY() * SIZE_Z, SIZE_X, SIZE_Z);
    }

    /**
     * @return the coordinates
     */
    public ImmutableVector2i getCoords() {
        return coords;
    }

    /**
     * @param dir the orientation which gives the neighbor index in [0..8]
     * @return the neighbor sector
     */
    public Sector getNeighbor(Orientation dir) {

        ImmutableVector2i v = dir.getDir();
        int x = coords.getX() + v.getX();
        int z = coords.getY() + v.getY();

        return Sectors.getSector(new Vector2i(x, z));
    }

    @Override
    public String toString() {
        return "Sector [" + coords.getX() + ", " + coords.getY() + "]";
    }

    @Override
    public int hashCode() {
        return coords.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Sector other = (Sector) obj;

        // coords cannot be null
        return coords.equals(other.coords);
    }

    /**
     * @return the bounds in (block) world coordinates
     */
    public Rect2i getWorldBounds() {
        return bounds;
    }

}

