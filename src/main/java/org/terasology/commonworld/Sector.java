/*
 * Copyright 2013 MovingBlocks
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

import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.terasology.engine.world.block.BlockArea;
import org.terasology.engine.world.block.BlockAreac;

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

    private final Vector2i coords = new Vector2i();

    private final BlockArea bounds = new BlockArea(BlockArea.INVALID);


    /**
     * @param coords the coordinates
     */
    Sector(Vector2ic coords) {
        if (coords == null) {
            throw new NullPointerException("coords cannot be null");
        }

        this.coords.set(coords);
        this.bounds.set(0,0,0,0);
        this.bounds.setPosition(coords.x() * SIZE_X, coords.y() * SIZE_Z).setSize(SIZE_X, SIZE_Z);
    }

    /**
     * @return the coordinates
     */
    public Vector2ic getCoords() {
        return coords;
    }

    /**
     * @param dir the orientation which gives the neighbor index in [0..8]
     * @return the neighbor sector
     */
    public Sector getNeighbor(Orientation dir) {

        Vector2ic v = dir.direction();
        int x = coords.x() + v.x();
        int z = coords.y() + v.y();

        return new Sector(new Vector2i(x, z));
    }

    @Override
    public String toString() {
        return "Sector [" + coords.x() + ", " + coords.y() + "]";
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
    public BlockAreac getWorldBounds() {
        return bounds;
    }

}

