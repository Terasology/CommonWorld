// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld.heightmap;

import org.terasology.engine.utilities.procedural.BrownianNoise;
import org.terasology.engine.utilities.procedural.Noise;
import org.terasology.engine.utilities.procedural.SimplexNoise;

/**
 * A simple implementation based on {@link SimplexNoise}
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
