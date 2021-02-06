// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld.heightmap;

import com.google.common.math.IntMath;
import org.terasology.commonworld.array.IntArray2D;
import org.terasology.commonworld.symmetry.Symmetry;
import org.terasology.joml.geom.Rectanglei;

import java.util.List;

/**
 * Provides access to different height maps
 */
public final class HeightMaps {

    private HeightMaps() {
        // avoid instantiation
    }

    /**
     * @param hm the height to use
     * @param area the area to cache
     * @param scale the scale level (should be a divisor of area.width and area.height)
     * @return An height map based on the given constant value
     */
    public static HeightMap caching(HeightMap hm, Rectanglei area, int scale) {
        if (scale == 1) {
            return new CachingHeightMap(area, hm);
        } else {
            return new CachingLerpHeightMap(area, hm, scale);
        }
    }

    /**
     * @param height the height to use
     * @return An height map based on the given constant value
     */
    public static HeightMap constant(int height) {
        return new ConstantHeightMap(height);
    }

    /**
     * @param hm the backing height map
     * @param offset the height offset to use
     * @return An height map that returns all values with +offset
     */
    public static HeightMap offset(HeightMap hm, int offset) {
        return new OffsetHeightMap(hm, offset);
    }

    /**
     * @param hm the backing height map
     * @param scale the scale factor to use
     * @return An height map that returns values at (x * scale, z * scale)
     */
    public static HeightMap scalingArea(final HeightMap hm, final int scale) {
        return new HeightMap() {

            @Override
            public int apply(int x, int z) {
                return hm.apply(x * scale, z * scale);
            }
        };
    }

    /**
     * @param hm the backing height map
     * @param scale the scale factor to use
     * @return An height map that returns y * scale
     */
    public static HeightMap scalingHeight(final HeightMap hm, final int scale) {
        return new HeightMap() {

            @Override
            public int apply(int x, int z) {
                return hm.apply(x, z) * scale;
            }
        };
    }

    /**
     * @param data the data
     * @return An height map that returns the content of the string list
     */
    public static HeightMap stringBased(List<String> data) {
        return new StringHeightMap(data);
    }

    /**
     * @param hm the height map
     * @param sym the symmetry
     * @return a symmetric height map
     */
    public static HeightMap symmetric(final HeightMap hm, final Symmetry sym) {
        return new HeightMap() {
            @Override
            public int apply(int x, int z) {
                if (sym.isMirrored(x, z)) {
                    return hm.apply(sym.getMirrored(x, z));
                } else {
                    return hm.apply(x, z);
                }
            }

        };
    }

    /**
     * @param array the underlying array
     * @return a height map that is mirror along the diagonal (1, -1)
     */
    public static HeightMap fromArray2D(final IntArray2D array) {
        return new HeightMap() {

            @Override
            public int apply(int x, int z) {
                int lx = IntMath.mod(x, array.getWidth());
                int lz = IntMath.mod(z, array.getHeight());
                return array.get(lx, lz);
            }
        };
    }
}
