/*
 * Copyright 2014 MovingBlocks
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

package org.terasology.commonworld.symmetry;

import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Vector2i;

/**
 * Defines a symmetry
 */
public interface Symmetry {

    /**
     * @param v the position coordinate
     * @return true if the location is on the "mirrored" part of the height map
     */
    boolean isMirrored(BaseVector2i v);

    /**
     * @param x the x coordinate
     * @param z the z coordinate
     * @return true if the location is on the "mirrored" part of the height map
     */
    boolean isMirrored(int x, int z);

    /**
     * @param sc the position coordinates
     * @return the mirrored position
     */
    Vector2i getMirrored(BaseVector2i sc);

    /**
     * @param x the x position coordinate
     * @param z the z position coordinate
     * @return the mirrored position
     */
    Vector2i getMirrored(int x, int z);

}
