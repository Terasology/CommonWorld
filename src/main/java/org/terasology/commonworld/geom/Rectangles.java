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

package org.terasology.commonworld.geom;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import org.terasology.commonworld.Orientation;

import com.google.common.base.Preconditions;

/**
 * Rectangle-related utility methods
 * @author Martin Steiger
 */
public final class Rectangles {

    private Rectangles() {
        // private
    }

    /**
     * @param rc the original rectangle
     * @param ext the amount to add in all directions
     * @return a new rectangle that is expanded in all directions
     */
    public static Rectangle expandRect(Rectangle rc, int ext) {
        int x = rc.x - ext;
        int y = rc.y - ext;
        int width = rc.width + 2 * ext;
        int height = rc.height + 2 * ext;
        return new Rectangle(x, y, width, height);
    }

    /**
     * @param rc the rectangle to transform
     * @param bounds the bounding rectangle for the transformation (translation offset and rotation center)
     * @param center the center of the original placing rect
     * @param rot the rotation in degrees (only multiples of 45deg.)
     * @return the translated and rotated rectangle
     */
    public static Rectangle transformRect(Rectangle rc, Rectangle bounds, Point center, int rot) {

        double anchorx = bounds.width * 0.5;
        double anchory = bounds.height * 0.5;

        AffineTransform at = new AffineTransform();
        at.translate(bounds.x, bounds.y);
        at.translate(anchorx, anchory);
        at.rotate(Math.toRadians(rot));
        at.translate(-center.x, -center.y);

        Point ptSrc1 = new Point(rc.x, rc.y);
        Point ptSrc2 = new Point(rc.x + rc.width, rc.y + rc.height);
        Point ptDst1 = new Point();
        Point ptDst2 = new Point();
        at.transform(ptSrc1, ptDst1);
        at.transform(ptSrc2, ptDst2);

        int x = Math.min(ptDst1.x, ptDst2.x);
        int y = Math.min(ptDst1.y, ptDst2.y);
        int width = Math.max(ptDst1.x, ptDst2.x) - x;
        int height = Math.max(ptDst1.y, ptDst2.y) - y;
        Rectangle result = new Rectangle(x, y, width, height);

        return result;
    }

    /**
     * @param rc the original rectangle
     * @param o the position of the border of interest
     * @return a rectangle of thickness 1 at the specified border edge
     */
    public static Rectangle getBorder(Rectangle rc, Orientation o) {
        Preconditions.checkArgument(o.isCardinal(), "Orientation must be NORTH, WEST, SOUTH or EAST");

        if (o == Orientation.NORTH) {
            int x = rc.x;
            int y = rc.y;
            return new Rectangle(x, y, rc.width, 1);

        } else if (o == Orientation.SOUTH) {
            int x = rc.x;
            int y = rc.y + rc.height - 1;
            return new Rectangle(x, y, rc.width, 1);

        } else if (o == Orientation.WEST) {
            int x = rc.x;
            int y = rc.y;
            return new Rectangle(x, y, 1, rc.height);

        } else if (o == Orientation.EAST) {
            int x = rc.x + rc.width - 1;
            int y = rc.y;
            return new Rectangle(x, y, 1, rc.height);
        }

        throw new IllegalStateException();
    }
}
