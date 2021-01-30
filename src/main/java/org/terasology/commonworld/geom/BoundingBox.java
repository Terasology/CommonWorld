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

import com.google.common.base.Optional;
import org.joml.Vector2ic;
import org.terasology.world.block.BlockArea;

import java.awt.Rectangle;
import java.util.Collection;

/**
 * Defines a axis-aligned bounding box based on a set of Vector2is
 * use {@link org.terasology.world.block.BlockRegion}
 */
@Deprecated
public class BoundingBox {
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private boolean empty = true;

    /**
     * Default, empty constructor
     */
    public BoundingBox() {
        x1 = Integer.MAX_VALUE;
        y1 = Integer.MAX_VALUE;
        x2 = Integer.MIN_VALUE;
        y2 = Integer.MIN_VALUE;
    }

    /**
     * @param pt the initial Vector2i
     */
    public BoundingBox(Vector2ic pt) {
        x1 = pt.x();
        y1 = pt.y();
        x2 = pt.x();
        y2 = pt.y();

        empty = false;
    }

    /**
     * @param pts the initial Vector2is
     */
    public BoundingBox(Collection<? extends Vector2ic> pts) {
        this();
        addAll(pts);
    }

    /**
     * @param pts a collection of Vector2is
     * @return the bounding rectangle of pts or absent() if pts is empty
     */
    public static Optional<Rectangle> getBoundingRect(Collection<? extends Vector2ic> pts) {
        if (pts.isEmpty()) {
            return Optional.absent();
        }

        BoundingBox bbox = new BoundingBox(pts);
        return Optional.of(bbox.toRectangle());
    }

    /**
     * Resizes to include pt
     * @param pt the Vector2i
     */
    public void add(Vector2ic pt) {
        add(pt.x(), pt.y());
    }

    /**
     * Resizes to include all Vector2is
     * @param pts a collection of Vector2is
     */
    public void addAll(Collection<? extends Vector2ic> pts) {
        for (Vector2ic pt : pts) {
            add(pt.x(), pt.y());
        }
    }

    /**
     * Resizes to include (x, y)
     * @param x the x coord.
     * @param y the y coord.
     */
    public void add(int x, int y) {
        if (x < x1) {
            x1 = x;
        }

        if (y < y1) {
            y1 = y;
        }

        if (x > x2) {
            x2 = x;
        }

        if (y > y2) {
            y2 = y;
        }

        empty = false;
    }

    /**
     * @return true if the bounding box is empty
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * @return a new Rect2i instance containing the bounds
     */
    public BlockArea toRect2i() {
        if (empty) {
            throw new IllegalStateException("BoundingBox undefined - no Points were added");
        }

        return new BlockArea(x1, y1, x2, y2);
    }

    /**
     * @return a new Rectangle containing the bounds
     */
    public Rectangle toRectangle() {
        if (empty) {
            throw new IllegalStateException("BoundingBox undefined - no Points were added");
        }

        return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }

}
