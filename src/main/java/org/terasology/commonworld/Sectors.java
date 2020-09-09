// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld;

import com.google.common.collect.Maps;
import com.google.common.math.IntMath;
import org.terasology.math.TeraMath;
import org.terasology.math.geom.Vector2i;
import org.terasology.math.geom.Vector3f;

import java.math.RoundingMode;
import java.util.Map;

/**
 * Gives access to all Sectors
 */
public final class Sectors {

    private static final Map<Vector2i, Sector> SECTORS = Maps.newConcurrentMap();

    private Sectors() {
        // private
    }

    /**
     * @param x the x coordinate of the sector
     * @param z the z coordinate of the sector
     * @return the sector
     */
    public static Sector getSector(int x, int z) {
        return getSector(new Vector2i(x, z));
    }

    /**
     * @param coord the coordinate of the sector
     * @return the sector
     */
    public static Sector getSector(Vector2i coord) {
        Sector sector = SECTORS.get(coord);

        if (sector == null) {
            sector = new Sector(coord);

            SECTORS.put(coord, sector);
        }

        return sector;
    }

    /**
     * @param pos the world position
     * @return the sector
     */
    public static Sector getSectorForPosition(Vector3f pos) {
        return getSectorForBlock(TeraMath.floorToInt(pos.getX()), TeraMath.floorToInt(pos.getZ()));
    }

    /**
     * @param wx the world block x coord
     * @param wz the world block z coord
     * @return the sector
     */
    public static Sector getSectorForBlock(int wx, int wz) {
        int sx = IntMath.divide(wx, Sector.SIZE, RoundingMode.FLOOR);
        int sz = IntMath.divide(wz, Sector.SIZE, RoundingMode.FLOOR);

        return getSector(new Vector2i(sx, sz));
    }
}
