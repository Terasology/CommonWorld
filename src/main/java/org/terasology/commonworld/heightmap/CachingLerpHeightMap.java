// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld.heightmap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.joml.geom.Rectanglei;
import org.terasology.math.TeraMath;

/**
 * A cache that stores a rectangular area and interpolates values bi-linearly
 */
class CachingLerpHeightMap implements HeightMap {

    private static final Logger logger = LoggerFactory.getLogger(CachingLerpHeightMap.class);

    private final short[] height;
    private final Rectanglei area;
    private final HeightMap hm;
    private final int scale;
    private int scaledWidth;
    private int scaledHeight;

    /**
     * @param area the area to cache
     * @param hm the height map to use
     * @param scale the scale level
     */
    public CachingLerpHeightMap(Rectanglei area, HeightMap hm, int scale) {
        this.area = area;
        this.scale = scale;
        this.hm = hm;

        this.scaledWidth = area.getSizeX() / scale + 1;
        this.scaledHeight = area.getSizeY() / scale + 1;

        // if scale is not a divisor of the width -> round up
        if (area.getSizeX() % scale > 0) {
            scaledWidth++;
        }

        // if scale is not a divisor of the height -> round up
        if (area.getSizeY() % scale > 0) {
            scaledHeight++;
        }

        this.height = new short[scaledWidth * scaledHeight];

        // area is 1 larger
        for (int z = 0; z < scaledHeight; z++) {
            for (int x = 0; x < scaledWidth; x++) {
                int y = hm.apply(area.minX() + x * scale, area.minY() + z * scale);
                height[z * scaledWidth + x] = (short) y;
            }
        }
    }

    @Override
    public int apply(int x, int z) {
        boolean xOk = (x >= area.minX()) && (x <= area.maxX());
        boolean zOk = (z >= area.minY()) && (z <= area.maxY());

        if (xOk && zOk) {

            double lx = (x - area.minX()) / (double) scale;
            double lz = (z - area.minY()) / (double) scale;

            int minX = TeraMath.floorToInt(lx);
            int maxX = minX + 1;

            int minZ = TeraMath.floorToInt(lz);
            int maxZ = minZ + 1;

            int q00 = getHeight(minX, minZ);
            int q10 = getHeight(maxX, minZ);
            int q01 = getHeight(minX, maxZ);
            int q11 = getHeight(maxX, maxZ);

            double ipx = lx - minX;
            double ipz = lz - minZ;

            double min = TeraMath.lerp(q00, q10, ipx);
            double max = TeraMath.lerp(q01, q11, ipx);

            double res = TeraMath.lerp(min, max, ipz);

            return TeraMath.floorToInt(res + 0.49);
        }

        logger.debug("Accessing height map outside cached bounds -- referring to uncached height map");

        return hm.apply(x, z);
    }

    private int getHeight(int lx, int lz) {
        return height[lz * scaledWidth + lx];
    }

}
