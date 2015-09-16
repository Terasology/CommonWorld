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

import org.terasology.utilities.procedural.BrownianNoise;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.SimplexNoise;

/**
 * A simple implementation based on {@link SimplexNoise}
 * @author Martin Steiger
 */
public class NoiseHeightMap implements HeightMap {

    private Noise terrainNoise;

    /**
     * @param seed the seed value
     */
    public NoiseHeightMap(long seed) {
        setSeed(seed);
    }

    /**
     * @param seed the seed value
     */
    public void setSeed(long seed) {
        terrainNoise = new BrownianNoise(new SimplexNoise(seed), 6);
    }

    @Override
    public int apply(int x, int z) {
        int val = 7;
        val += (int) (terrainNoise.noise(x / 1000f, z / 1000f) * 8f);

        if (val < 1) {
            val = 1;
        }

        return val;
    }


}
