/*
 * Copyright 2015 MovingBlocks
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
package org.terasology.commonworld.geom;

import org.terasology.math.Vector2i;

import java.util.EnumSet;
import java.util.Set;

/**
 * Created by Skaldarnar on 07.03.2015.
 */
public class BresenhamLineIterator {

    /**
     * Overlap mode
     */
    public static enum Overlap {
        /**
         * Overlap - first go major then minor direction
         */
        MAJOR,

        /**
         * Overlap - first go minor then major direction
         */
        MINOR
    }

    /**
     * Thickness mode
     */
    public static enum ThicknessMode {
        /**
         * Line goes through the center
         */
        MIDDLE,

        /**
         * Line goes along the border (clockwise)
         */
        CLOCKWISE,

        /**
         * Line goes along the border (counter-clockwise)
         */
        COUNTERCLOCKWISE
    }

    /**
     * Classical Bresenham line drawing algorithm without overlapping pixels.
     *
     * @param start the start position
     * @param end the end position
     */
    public static void iterateLine2D(Vector2i start, Vector2i end, BresenhamVisitor visitor) {
        iterateLine2D(start, end, visitor, EnumSet.noneOf(Overlap.class));
    }

    /**
     * Modified Bresenham line drawing algorithm with optional overlap (esp. for drawThickLine())
     * Overlap draws additional pixel when changing minor direction - for standard bresenham overlap = LINE_OVERLAP_NONE (0)
     * <pre>
     *  Sample line:
     *    00+
     *     -0000+
     *         -0000+
     *             -00
     *  0 pixels are drawn for normal line without any overlap
     *  + pixels are drawn if LINE_OVERLAP_MAJOR
     *  - pixels are drawn if LINE_OVERLAP_MINOR
     * </pre>
     *
     * @param start the start position
     * @param end the end position
     * @param overlap the overlap specification
     */
    public static void iterateLine2D(Vector2i start, Vector2i end, BresenhamVisitor visitor, Set<Overlap> overlap) {
        iterateLine2D(start.x, start.y, end.x, end.y, visitor, overlap);
    }

    public static void iterateLine2D(int aXStart, int aYStart, int xEnd, int yEnd, BresenhamVisitor visitor, Set<Overlap> overlap) {
        int tDeltaX;
        int tDeltaY;
        int tDeltaXTimes2;
        int tDeltaYTimes2;
        int tError;
        int tStepX;
        int tStepY;

        if ((aXStart == xEnd) || (aYStart == yEnd)) {
            //horizontal or vertical line -> directly add all points
            int sx = Math.min(aXStart, xEnd);
            int sy = Math.min(aYStart, yEnd);
            int ex = Math.max(aXStart, xEnd);
            int ey = Math.max(aYStart, yEnd);

            for (int y = sy; y <= ey; y++) {
                for (int x = sx; x <= ex; x++) {
                    visitor.visit(x, y);
                }
            }
        } else {
            //calculate direction
            tDeltaX = xEnd - aXStart;
            tDeltaY = yEnd - aYStart;
            if (tDeltaX < 0) {
                tDeltaX = -tDeltaX;
                tStepX = -1;
            } else {
                tStepX = +1;
            }
            if (tDeltaY < 0) {
                tDeltaY = -tDeltaY;
                tStepY = -1;
            } else {
                tStepY = +1;
            }
            tDeltaXTimes2 = tDeltaX << 1;
            tDeltaYTimes2 = tDeltaY << 1;
            // add start pixel
            visitor.visit(aXStart, aYStart);
            if (tDeltaX > tDeltaY) {
                // start value represents a half step in Y direction
                tError = tDeltaYTimes2 - tDeltaX;
                while (aXStart != xEnd) {
                    // step in main direction
                    aXStart += tStepX;
                    if (tError >= 0) {
                        if (overlap.contains(Overlap.MAJOR)) {
                            // draw pixel in main direction before changing
                            visitor.visit(aXStart, aYStart);
                        }
                        // change Y
                        aYStart += tStepY;
                        if (overlap.contains(Overlap.MINOR)) {
                            // draw pixel in minor direction before changing
                            visitor.visit(aXStart - tStepX, aYStart);
                        }
                        tError -= tDeltaXTimes2;
                    }
                    tError += tDeltaYTimes2;
                    visitor.visit(aXStart, aYStart);
                }
            } else {
                tError = tDeltaXTimes2 - tDeltaY;
                while (aYStart != yEnd) {
                    aYStart += tStepY;
                    if (tError >= 0) {
                        if (overlap.contains(Overlap.MAJOR)) {
                            // draw pixel in main direction before changing
                            visitor.visit(aXStart, aYStart);
                        }
                        aXStart += tStepX;
                        if (overlap.contains(Overlap.MINOR)) {
                            // draw pixel in minor direction before changing
                            visitor.visit(aXStart, aYStart - tStepY);
                        }
                        tError -= tDeltaYTimes2;
                    }
                    tError += tDeltaXTimes2;
                    visitor.visit(aXStart, aYStart);
                }
            }
        }
    }
}
