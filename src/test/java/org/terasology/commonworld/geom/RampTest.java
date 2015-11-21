/*
 * Copyright 2015 MovingBlocks
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

package org.terasology.commonworld.geom;

import org.junit.Assert;
import org.junit.Test;
import org.terasology.math.geom.BaseVector3f;
import org.terasology.math.geom.Vector3f;

/**
 * Tests the {@link Ramp} class.
 */
public class RampTest {

    private double eps = 0.001;

    @Test
    public void simpleTest() {
        BaseVector3f a = new Vector3f(10, 0, 10);
        BaseVector3f b = new Vector3f(10, 1, 20);
        Ramp ramp = new Ramp(a, b);

        Assert.assertEquals(0.2, ramp.getY(10, 12), eps);
        Assert.assertEquals(0.9, ramp.getY(10, 19), eps);

        Assert.assertEquals(-0.1, ramp.getY(10, 9), eps);
        Assert.assertEquals(1.1, ramp.getY(10, 21), eps);

        Assert.assertEquals(0.0, ramp.getClampedY(10, 9), eps);
        Assert.assertEquals(1.0, ramp.getClampedY(10, 21), eps);

        Assert.assertEquals(0.3, ramp.getY(18, 13), eps);
        Assert.assertEquals(0.3, ramp.getY(79, 13), eps);

        Assert.assertEquals(1, Math.abs(ramp.getKappaNorm(9, 13)), eps);
        Assert.assertEquals(1, Math.abs(ramp.getKappaNorm(11, 13)), eps);
    }
}
