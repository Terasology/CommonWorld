// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld.heightmap;

import org.terasology.math.geom.BaseVector2i;

import java.util.function.Function;

/**
 * Definition of a height map
 */
public interface HeightMap extends Function<BaseVector2i, Integer> {

    @Override
    default Integer apply(BaseVector2i input) {
        return Integer.valueOf(apply(input.getX(), input.getY()));
    }

    /**
     * @param x the x world coord
     * @param z the z world coord
     * @return the height
     */
    int apply(int x, int z);
}
