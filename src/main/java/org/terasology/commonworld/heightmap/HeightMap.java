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

package org.terasology.commonworld.heightmap;

import java.util.function.Function;

import org.joml.Vector2ic;
import org.terasology.math.geom.BaseVector2i;

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
