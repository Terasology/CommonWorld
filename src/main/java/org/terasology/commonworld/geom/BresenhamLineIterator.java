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

import java.util.EnumSet;
import java.util.Set;

public final class BresenhamLineIterator {

    private BresenhamLineIterator() {
        // no instances
    }

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
     * @param x0 x0 x-coordinate of starting position
     * @param y0 y0 y-coordinate of starting position
     * @param x1 x1 x-coordinate of ending position
     * @param y1 y1 y-coordinate of ending position
     */
    public static void iterateLine2D(int x0, int y0, int x1, int y1, BresenhamVisitor visitor) {
        iterateLine2D(x0, y0, x1, y1, visitor, EnumSet.noneOf(Overlap.class));
    }

    /**
     * Modified Bresenham line drawing algorithm with optional overlap (esp. for iterateThickLine2D())
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
     * @param aXStart x-coordinate of the start position
     * @param aYStart y-coordinate of the start position
     * @param aXEnd x-coordinate of the end position
     * @param aYEnd y-coordinate of the end position
     * @param overlap the overlap specification
     */
    public static void iterateLine2D(int aXStart, int aYStart, int aXEnd, int aYEnd, BresenhamVisitor visitor, Set<Overlap> overlap) {
        int tDeltaX;
        int tDeltaY;
        int tDeltaXTimes2;
        int tDeltaYTimes2;
        int tError;
        int tStepX;
        int tStepY;

        if ((aXStart == aXEnd) || (aYStart == aYEnd)) {
            //horizontal or vertical line -> directly add all points
            int sx = Math.min(aXStart, aXEnd);
            int sy = Math.min(aYStart, aYEnd);
            int ex = Math.max(aXStart, aXEnd);
            int ey = Math.max(aYStart, aYEnd);

            for (int y = sy; y <= ey; y++) {
                for (int x = sx; x <= ex; x++) {
                    visitor.visit(x, y);
                }
            }
        } else {
            //calculate direction
            tDeltaX = aXEnd - aXStart;
            tDeltaY = aYEnd - aYStart;
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
                while (aXStart != aXEnd) {
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
                while (aYStart != aYEnd) {
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

    /**
     * Bresenham with thickness - no pixel missed and every pixel only visited once!
     * @param x0 x0 x-coordinate of starting position
     * @param y0 y0 y-coordinate of starting position
     * @param x1 x1 x-coordinate of ending position
     * @param y1 y1 y-coordinate of ending position
     * @param thickness the thickness
     * @param mode the thickness mode
     */
    public static void iterateThickLine2D(int x0, int y0, int x1, int y1, BresenhamVisitor visitor, int thickness, ThicknessMode mode) {

        if (thickness <= 1) {
            iterateLine2D(x0, y0, x1, y1, visitor, EnumSet.noneOf(Overlap.class));
        }

        int tDeltaX;
        int tDeltaY;
        int tDeltaXTimes2;
        int tDeltaYTimes2;
        int tError;
        int tStepX;
        int tStepY;

        int aXStart = x0;
        int aYStart = y0;
        int aXEnd = x1;
        int aYEnd = y1;

        /**
         * For coordinate system with 0.0 located at top-left.
         * Swap X and Y delta and calculate clockwise (new delta X inverted)
         * or counterclockwise (new delta Y inverted) rectangular direction.
         * The right rectangular direction for LineOverlap.MAJOR toggles with each octant
         */
        tDeltaY = aXEnd - aXStart;
        tDeltaX = aYEnd - aYStart;
        // mirror 4 quadrants to one and adjust deltas and stepping direction
        boolean tSwap = true; // count effective mirroring
        if (tDeltaX < 0) {
            tDeltaX = -tDeltaX;
            tStepX = -1;
            tSwap = !tSwap;
        } else {
            tStepX = +1;
        }
        if (tDeltaY < 0) {
            tDeltaY = -tDeltaY;
            tStepY = -1;
            tSwap = !tSwap;
        } else {
            tStepY = +1;
        }
        tDeltaXTimes2 = tDeltaX << 1;
        tDeltaYTimes2 = tDeltaY << 1;
        Set<Overlap> tOverlap;

        // adjust for right direction of thickness from line origin
        int tDrawStartAdjustCount;

        switch (mode) {
            case COUNTERCLOCKWISE:
                tDrawStartAdjustCount = thickness - 1;
                break;
            case CLOCKWISE:
                tDrawStartAdjustCount = 0;
                break;
            case MIDDLE:
                tDrawStartAdjustCount = thickness / 2;
                break;
            default:
                throw new IllegalStateException();
        }

        // which octant are we now
        if (tDeltaX >= tDeltaY) {
            if (tSwap) {
                tDrawStartAdjustCount = (thickness - 1) - tDrawStartAdjustCount;
                tStepY = -tStepY;
            } else {
                tStepX = -tStepX;
            }
            /*
             * Vector for draw direction of lines is rectangular and counterclockwise to original line
             * Therefore no pixel will be missed if LINE_OVERLAP_MAJOR is used
             * on changing in minor rectangular direction
             */
            // adjust draw start point
            tError = tDeltaYTimes2 - tDeltaX;
            for (int i = tDrawStartAdjustCount; i > 0; i--) {
                // change X (main direction here)
                aXStart -= tStepX;
                aXEnd -= tStepX;
                if (tError >= 0) {
                    // change Y
                    aYStart -= tStepY;
                    aYEnd -= tStepY;
                    tError -= tDeltaXTimes2;
                }
                tError += tDeltaYTimes2;
            }
            //iterate start line
            iterateLine2D(aXStart, aYStart, aXEnd, aYEnd, visitor);
            // draw aThickness lines
            tError = tDeltaYTimes2 - tDeltaX;
            for (int i = thickness; i > 1; i--) {
                // change X (main direction here)
                aXStart += tStepX;
                aXEnd += tStepX;
                tOverlap = EnumSet.noneOf(Overlap.class);
                if (tError >= 0) {
                    // change Y
                    aYStart += tStepY;
                    aYEnd += tStepY;
                    tError -= tDeltaXTimes2;
                    /*
                     * change in minor direction reverse to line (main) direction
                     * because of chosing the right (counter)clockwise draw vector
                     * use LINE_OVERLAP_MAJOR to fill all pixel
                     *
                     * EXAMPLE:
                     * 1,2 = Pixel of first lines
                     * 3 = Pixel of third line in normal line mode
                     * - = Pixel which will be drawn in LINE_OVERLAP_MAJOR mode
                     *           33
                     *       3333-22
                     *   3333-222211
                     * 33-22221111
                     *  221111                     /\
                         *  11                          Main direction of draw vector
                     *  -> Line main direction
                     *  <- Minor direction of counterclockwise draw vector
                     */
                    tOverlap = EnumSet.of(Overlap.MAJOR);
                }
                tError += tDeltaYTimes2;
                iterateLine2D(aXStart, aYStart, aXEnd, aYEnd, visitor, tOverlap);
            }
        } else {
            // the other octant
            if (tSwap) {
                tStepX = -tStepX;
            } else {
                tDrawStartAdjustCount = (thickness - 1) - tDrawStartAdjustCount;
                tStepY = -tStepY;
            }
            // adjust draw start point
            tError = tDeltaXTimes2 - tDeltaY;
            for (int i = tDrawStartAdjustCount; i > 0; i--) {
                aYStart -= tStepY;
                aYEnd -= tStepY;
                if (tError >= 0) {
                    aXStart -= tStepX;
                    aXEnd -= tStepX;
                    tError -= tDeltaYTimes2;
                }
                tError += tDeltaXTimes2;
            }
            //iteratestart line
            iterateLine2D(aXStart, aYStart, aXEnd, aYEnd, visitor);
            tError = tDeltaXTimes2 - tDeltaY;
            for (int i = thickness; i > 1; i--) {
                aYStart += tStepY;
                aYEnd += tStepY;
                tOverlap = EnumSet.noneOf(Overlap.class);
                if (tError >= 0) {
                    aXStart += tStepX;
                    aXEnd += tStepX;
                    tError -= tDeltaYTimes2;
                    tOverlap = EnumSet.of(Overlap.MAJOR);
                }
                tError += tDeltaXTimes2;
                iterateLine2D(aXStart, aYStart, aXEnd, aYEnd, visitor, tOverlap);
            }
        }
    }

    /**
     * Bresenham with thickness, but no clipping, some pixel are drawn twice (use LINE_OVERLAP_BOTH)
     * and direction of thickness changes for each octant (except for LINE_THICKNESS_MIDDLE and aThickness odd)
     * @param x0 x0
     * @param y0 y0
     * @param x1 x1
     * @param y1 y1
     * @param visitor the point visitor
     * @param thickness the thickness
     * @param mode the mode
     */
    public static void iterateThickLine2DSimple(int x0, int y0, int x1, int y1, BresenhamVisitor visitor, int thickness, ThicknessMode mode) {

        int tDeltaX;
        int tDeltaY;
        int tDeltaXTimes2;
        int tDeltaYTimes2;
        int tError;
        int tStepX;
        int tStepY;

        int aXStart = x0;
        int aYStart = y0;
        int aXEnd = x1;
        int aYEnd = y1;

        tDeltaY = aXStart - aXEnd;
        tDeltaX = aYEnd - aYStart;
        // mirror 4 quadrants to one and adjust deltas and stepping direction
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
        Set<Overlap> tOverlap;
        // which octant are we now
        if (tDeltaX > tDeltaY) {
            if (mode == ThicknessMode.MIDDLE) {
                // adjust draw start point
                tError = tDeltaYTimes2 - tDeltaX;
                for (int i = thickness / 2; i > 0; i--) {
                    // change X (main direction here)
                    aXStart -= tStepX;
                    aXEnd -= tStepX;
                    if (tError >= 0) {
                        // change Y
                        aYStart -= tStepY;
                        aYEnd -= tStepY;
                        tError -= tDeltaXTimes2;
                    }
                    tError += tDeltaYTimes2;
                }
            }
            //draw start line
            iterateLine2D(aXStart, aYStart, aXEnd, aYEnd, visitor);
            // draw aThickness lines
            tError = tDeltaYTimes2 - tDeltaX;
            for (int i = thickness; i > 1; i--) {
                // change X (main direction here)
                aXStart += tStepX;
                aXEnd += tStepX;
                tOverlap = EnumSet.noneOf(Overlap.class);
                if (tError >= 0) {
                    // change Y
                    aYStart += tStepY;
                    aYEnd += tStepY;
                    tError -= tDeltaXTimes2;
                    tOverlap = EnumSet.of(Overlap.MINOR, Overlap.MAJOR);
                }
                tError += tDeltaYTimes2;
                iterateLine2D(aXStart, aYStart, aXEnd, aYEnd, visitor, tOverlap);
            }
        } else {
            // adjust draw start point
            if (mode == ThicknessMode.MIDDLE) {
                tError = tDeltaXTimes2 - tDeltaY;
                for (int i = thickness / 2; i > 0; i--) {
                    aYStart -= tStepY;
                    aYEnd -= tStepY;
                    if (tError >= 0) {
                        aXStart -= tStepX;
                        aXEnd -= tStepX;
                        tError -= tDeltaYTimes2;
                    }
                    tError += tDeltaXTimes2;
                }
            }
            //draw start line
            iterateLine2D(aXStart, aYStart, aXEnd, aYEnd, visitor);
            tError = tDeltaXTimes2 - tDeltaY;
            for (int i = thickness; i > 1; i--) {
                aYStart += tStepY;
                aYEnd += tStepY;
                tOverlap = EnumSet.noneOf(Overlap.class);
                if (tError >= 0) {
                    aXStart += tStepX;
                    aXEnd += tStepX;
                    tError -= tDeltaYTimes2;
                    tOverlap = EnumSet.of(Overlap.MINOR, Overlap.MAJOR);
                }
                tError += tDeltaXTimes2;
                iterateLine2D(aXStart, aYStart, aXEnd, aYEnd, visitor, tOverlap);
            }
        }
    }
}
