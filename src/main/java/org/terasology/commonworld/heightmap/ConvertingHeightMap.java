// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld.heightmap;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Converts height map data w.r.t. a map-based conversion
 */
public class ConvertingHeightMap implements HeightMap {

    private final Map<Integer, Integer> converter = Maps.newHashMap();

    private final HeightMap heightMap;

    /**
     * @param heightMap the base height map
     */
    public ConvertingHeightMap(HeightMap heightMap) {
        this.heightMap = heightMap;
    }

    /**
     * Adds a conversion
     * @param from from
     * @param to to
     */
    public void addConversion(Integer from, Integer to) {
        converter.put(from, to);
    }

    @Override
    public int apply(int x, int z) {
        int val = heightMap.apply(x, z);
        Integer result = converter.get(val);

        if (result != null) {
            return result;
        }

        return val;
    }
}
