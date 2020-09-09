// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld.geom;

/**
 * Draws and/or fills circles
 */
public final class BresenhamCircleIterator {

    private BresenhamCircleIterator() {
    }

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
     * Horn's algorithm B. K. P. Horn: Circle Generators for Display Devices. Computer Graphics and Image Processing 5,
     * 2 (June 1976)
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
     *
     * @param cx x-coordinate of the center
     * @param cy y-coordinate of the center y
     * @param rad the radius
     * @param visitor the visitor
     * @param filled weather the inner pixels of the circle are visited or not
     * @see org.terasology.commonworld.geom.BresenhamCircleIterator#iterateCircle(int, int, int, BresenhamVisitor)
     * @see org.terasology.commonworld.geom.BresenhamCircleIterator#iterateFilledCircle(int, int, int,
     *         BresenhamVisitor)
     */
    public static void iterateCircle(int cx, int cy, int rad, BresenhamVisitor visitor, boolean filled) {
        if (filled) {
            iterateFilledCircle(cx, cy, rad, visitor);
        } else {
            iterateCircle(cx, cy, rad, visitor);
        }
    }
}
