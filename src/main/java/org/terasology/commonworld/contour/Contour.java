// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld.contour;

import com.google.common.collect.Lists;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Stores information on a contour
 */
public class Contour {

    /**
     * It would be nice a have a list implementation where contains() runs in O(1). Unfortunately, LinkedHashSet removes
     * duplicate entries.
     */
    private final List<Point> points = new ArrayList<Point>();
    private Collection<Point> simplifiedPoints;
    private Polygon polygon;

    /**
     * @param pts the point collection to use
     * @return an AWT polygon of the data
     */
    private static Polygon createPolygon(Collection<Point> pts) {
        int m = pts.size();

        int[] xPoints = new int[m];
        int[] yPoints = new int[m];
        int idx = 0;
        for (Point cpt : pts) {
            xPoints[idx] = cpt.x;
            yPoints[idx] = cpt.y;
            idx++;
        }
        return new Polygon(xPoints, yPoints, m);
    }

    /**
     * Removes all points that lie on straight lines
     *
     * @param pts the list of points
     * @return a <b>new collection</b> containing the points
     */
    private static List<Point> simplify(List<Point> pts) {

        if (pts.size() < 2) {
            return Lists.newArrayList(pts);
        }

        List<Point> result = Lists.newArrayList();

        Point first = pts.iterator().next();
        Point last = pts.get(pts.size() - 1);
        Point prev = last;
        Point prevDir = new Point(first.x - last.x, first.y - last.y);

        for (Point p : pts) {
            Point newdir = new Point(p.x - prev.x, p.y - prev.y);
            if (!newdir.equals(prevDir)) {
                result.add(prev);
                prevDir = newdir;
            }
            prev = p;
        }

        // do the same with the last point in the collection
        Point newdir = new Point(first.x - prev.x, first.y - prev.y);
        if (!newdir.equals(prevDir)) {
            result.add(prev);
        }

        return result;
    }

    /**
     * @param n the point to add
     */
    public void addPoint(Point n) {
        points.add(new Point(n));

        simplifiedPoints = null;
        polygon = null;
    }

    /**
     * @return an unmodifiable sorted view on the points
     */
    public Collection<Point> getPoints() {
        return Collections.unmodifiableCollection(points);
    }

    /**
     * @return a simplified version of the curve, containing only points at direction changes
     */
    public Collection<Point> getSimplifiedCurve() {
        if (simplifiedPoints == null) {
            simplifiedPoints = simplify(points);
        }

        return simplifiedPoints;
    }

    /**
     * @return a polygon representing the curve
     */
    public Polygon getPolygon() {
        if (polygon == null) {
            polygon = createPolygon(getSimplifiedCurve());
        }

        return polygon;
    }

    /**
     * A point is considered to lie inside if and only if:
     * <ul>
     * <li> it lies completely inside the boundary <i>or</i>
     * <li>
     * it lies exactly on the boundary and the
     * space immediately adjacent to the
     * point in the increasing <code>X</code> direction is
     * entirely inside the boundary <i>or</i>
     * <li>
     * it lies exactly on a horizontal boundary segment and the
     * space immediately adjacent to the point in the
     * increasing <code>Y</code> direction is inside the boundary.
     * </ul>
     *
     * @param x the x coord
     * @param y the y coord
     * @return true if inside
     */
    public boolean isInside(double x, double y) {
        return getPolygon().contains(x, y);
    }

    /**
     * @param tx the x translation
     * @param ty the y translation
     * @return the scaled contour
     */
    public Contour translate(int tx, int ty) {
        Contour c = new Contour();

        for (Point pt : points) {
            c.addPoint(new Point(pt.x + tx, pt.y + ty));
        }

        return c;
    }

    /**
     * @param scale the scale factor
     * @return the scaled contour
     */
    public Contour scale(int scale) {
        Contour c = new Contour();

        for (Point pt : points) {
            c.addPoint(new Point(pt.x * scale, pt.y * scale));
        }

        return c;
    }
}
