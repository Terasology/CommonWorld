// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld.geom;

import com.google.common.base.Optional;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;

import java.awt.Rectangle;
import java.util.Collection;

/**
 * Defines a axis-aligned bounding box based on a set of Vector2is
 */
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
    public BoundingBox(BaseVector2i pt) {
        x1 = pt.getX();
        y1 = pt.getY();
        x2 = pt.getX();
        y2 = pt.getY();

        empty = false;
    }

    /**
     * @param pts the initial Vector2is
     */
    public BoundingBox(Collection<? extends BaseVector2i> pts) {
        this();
        addAll(pts);
    }

    /**
     * @param pts a collection of Vector2is
     * @return the bounding rectangle of pts or absent() if pts is empty
     */
    public static Optional<Rectangle> getBoundingRect(Collection<? extends BaseVector2i> pts) {
        if (pts.isEmpty()) {
            return Optional.absent();
        }

        BoundingBox bbox = new BoundingBox(pts);
        return Optional.of(bbox.toRectangle());
    }

    /**
     * Resizes to include pt
     *
     * @param pt the Vector2i
     */
    public void add(BaseVector2i pt) {
        add(pt.getX(), pt.getY());
    }

    /**
     * Resizes to include all Vector2is
     *
     * @param pts a collection of Vector2is
     */
    public void addAll(Collection<? extends BaseVector2i> pts) {
        for (BaseVector2i pt : pts) {
            add(pt.getX(), pt.getY());
        }
    }

    /**
     * Resizes to include (x, y)
     *
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
    public Rect2i toRect2i() {
        if (empty) {
            throw new IllegalStateException("BoundingBox undefined - no Points were added");
        }

        return Rect2i.createFromMinAndMax(x1, y1, x2, y2);
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
