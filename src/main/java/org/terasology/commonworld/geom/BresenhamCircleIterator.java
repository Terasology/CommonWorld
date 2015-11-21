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

package org.terasology.commonworld.geom;

/**
 * Draws and/or fills circles
 */
public final class BresenhamCircleIterator {

    private BresenhamCircleIterator() { }

    /**
     * Visit all pixels inside the specified circle.
     *
     * @param cx x-coordinate of the center
     * @param cy y-coordinate of the center y
     * @param rad the radius
     * @param visitor the visitor
     */
    public static void iterateFilledCircle(int cx, int cy, int rad, BresenhamVisitor visitor) {
        for (int y = 0; y <= rad; y++) {
            for (int x = 0; x * x + y * y <= (rad + 0.5) * (rad + 0.5); x++) {
                visitor.visit(cx + x, cy + y);
                visitor.visit(cx - x, cy + y);
                visitor.visit(cx - x, cy - y);
                visitor.visit(cx + x, cy - y);
            }
        }
    }

    /**
     * Horn's algorithm B. K. P. Horn: Circle Generators for Display Devices.
     * Computer Graphics and Image Processing 5, 2 (June 1976)
     *
     * @param cx the center x
     * @param cy the center y
     * @param rad the radius
     * @param visitor the visitor
     */
    public static void iterateCircle(int cx, int cy, int rad, BresenhamVisitor visitor) {
        int d = -rad;
        int x = rad;
        int y = 0;
        while (y <= x) {
            visitor.visit(cx + x, cy + y);
            visitor.visit(cx - x, cy + y);
            visitor.visit(cx - x, cy - y);
            visitor.visit(cx + x, cy - y);

            visitor.visit(cx + y, cy + x);
            visitor.visit(cx - y, cy + x);
            visitor.visit(cx - y, cy - x);
            visitor.visit(cx + y, cy - x);

            d = d + 2 * y + 1;
            y = y + 1;
            if (d > 0) {
                d = d - 2 * x + 2;
                x = x - 1;
            }
        }
    }

    /**
     * Dispatches between the two specific circle iterations.
     * @see org.terasology.commonworld.geom.BresenhamCircleIterator#iterateCircle(int, int, int, BresenhamVisitor)
     * @see org.terasology.commonworld.geom.BresenhamCircleIterator#iterateFilledCircle(int, int, int, BresenhamVisitor)
     *
     * @param cx x-coordinate of the center
     * @param cy y-coordinate of the center y
     * @param rad the radius
     * @param visitor the visitor
     * @param filled weather the inner pixels of the circle are visited or not
     */
    public static void iterateCircle(int cx, int cy, int rad, BresenhamVisitor visitor, boolean filled) {
        if (filled) {
            iterateFilledCircle(cx, cy, rad, visitor);
        } else {
            iterateCircle(cx, cy, rad, visitor);
        }
    }
}
