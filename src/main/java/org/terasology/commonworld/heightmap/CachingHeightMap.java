// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld.heightmap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.joml.geom.Rectanglei;

/**
 * A cache that stores a rectangular area
 */
class CachingHeightMap implements HeightMap {

    private static final Logger logger = LoggerFactory.getLogger(CachingHeightMap.class);

    private final short[] height;
    private final Rectanglei area;
    private final HeightMap hm;

    /**
     * @param area the area to cache
     * @param hm the height map to use
     */
    public CachingHeightMap(Rectanglei area, HeightMap hm) {
        this.area = area;
        this.hm = hm;
        this.height = new short[area.getSizeX() * area.getSizeY()];

        for (int z = 0; z < area.getSizeY(); z++) {
            for (int x = 0; x < area.getSizeX(); x++) {
                int y = hm.apply(x + area.minX(), z + area.minY());
                height[z * area.getSizeX() + x] = (short) y;
            }
        }
    }

    @Override
    public int apply(int x, int z) {
        boolean xOk = x >= area.minX() && x < area.minX() + area.getSizeX();
        boolean zOk = z >= area.minY() && z < area.minY() + area.getSizeY();

        if (xOk && zOk) {
            int lx = x - area.minX();
            int lz = z - area.minY();
            return height[lz * area.getSizeX() + lx];
        }

        logger.debug("Accessing height map outside cached bounds -- referring to uncached height map");

        return hm.apply(x, z);
    }

}
