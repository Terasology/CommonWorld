/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.commonworld.symmetry;

import java.util.Iterator;

import org.terasology.math.Vector2i;

/**
 * An {@link Iterable} with infinite size that iterates in
 * a square-shapes spiral around the central point (inclusive).
 * <br/><br/>
 * The iteration starts in positive x direction.
 * @author Martin Steiger
 */
public class SpiralIterable implements Iterable<Vector2i> {

    private final Vector2i center;
    private final boolean clockwise;

    /**
     * Uses clock-wise iterations.
     * @param center the spiral center
     */
    public SpiralIterable(Vector2i center) {
        this(center, true);
    }

    /**
     * @param center the spiral center
     * @param clockwise true for clockwise iteration, false for counter-clockwise
     */
    public SpiralIterable(Vector2i center, boolean clockwise) {
        this.center = center;
        this.clockwise = clockwise;
    }

    @Override
    public Iterator<Vector2i> iterator() {

        return new Iterator<Vector2i>() {
            private int layer = 1;
            private int leg;
            private int x = -1;
            private int y;

            @Override
            public Vector2i next() {
                switch (leg) {
                    case 0:
                        ++x;
                        if (x == layer) {
                            ++leg;
                        }
                        break;
                    case 1:
                        ++y;
                        if (y == layer) {
                            ++leg;
                        }
                        break;
                    case 2:
                        --x;
                        if (-x == layer) {
                            ++leg;
                        }
                        break;
                    case 3:
                        --y;
                        if (-y == layer) {
                            leg = 0;
                            ++layer;
                        }
                        break;
                }

                return new Vector2i(center.getX() + x, center.getY() + (clockwise ? y : -y));
            }

            @Override
            public boolean hasNext() {
                return true;
            }
        };
    }
}
