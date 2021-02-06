// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld.heightmap;

import org.joml.Vector2ic;

import java.util.function.Function;

/**
 * Definition of a height map
 */
public interface HeightMap extends Function<Vector2ic, Integer> {

    @Override
    default Integer apply(Vector2ic input) {
        return Integer.valueOf(apply(input.x(), input.y()));
    }

    /**
     * @param x the x world coord
     * @param z the z world coord
     * @return the height
     */
    int apply(int x, int z);
}
