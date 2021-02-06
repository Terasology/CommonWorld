// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld.geom;

import org.joml.Vector3fc;
import org.joml.Vector3ic;
import org.terasology.math.TeraMath;

/**
 * A plane that is defined by two points.
 * The missing third point is derived by a horizontal (dy = 0) line.
 * The result is a ramp.
 * <pre>
 *  ( ex )   ( ax )     ( dx )     ( -dy )
 *  ( ey ) = ( ay ) + L ( dy ) + K (  0  )
 *  ( ez )   ( az )     ( dz )     (  dx )
 *
 *  ex = ax + L*dx - K*dy
 *
 *        ax + L*dx - ex
 *  K = ------------------
 *             dy
 *
 *
 *  ez = az + L*dz + K*dx
 *
 *        (ez*dz - az*dz - ax*dx + ex*dx)
 *  L = -----------------------------------
 *                  (dz^2 + dx^2)
 *
 *  ez = az * L*dz
 * </pre>
 */
public class Ramp {

    private final float ax;
    private final float ay;
    private final float az;
    private final float dx;
    private final float dy;
    private final float dz;

    /**
     * @param p0 the first point
     * @param p1 the second point
     */
    public Ramp(Vector3fc p0, Vector3fc p1) {
        ax = p0.x();
        ay = p0.y();
        az = p0.z();
        dx = p1.x() - p0.x();
        dy = p1.y() - p0.y();
        dz = p1.z() - p0.z();
    }

    /**
     * @param p0 the first point
     * @param p1 the second point
     */
    public Ramp(Vector3ic p0, Vector3ic p1) {
        ax = p0.x();
        ay = p0.y();
        az = p0.z();
        dx = p1.x() - p0.x();
        dy = p1.y() - p0.y();
        dz = p1.z() - p0.z();
    }

    /**
     * @param p0x the first point's x
     * @param p0y the first point's y
     * @param p0z the first point's z
     * @param p1x the second point's x
     * @param p1y the second point's y
     * @param p1z the second point's z
     */
    public Ramp(float p0x, float p0y, float p0z, float p1x, float p1y, float p1z) {
        ax = p0x;
        ay = p0y;
        az = p0z;
        dx = p1x - p0x;
        dy = p1y - p0y;
        dz = p1z - p0z;
    }

    /**
     * Finds the lambda value <b>along</b> the line. p0 has a lambda of 0, p1 has a lambda of 1
     * @param ex point x
     * @param ez point z
     * @return the lambda value
     */
    public float getLambda(float ex, float ez) {
        return (ez * dz - az * dz - ax * dx + ex * dx) / (dz * dz + dx * dx);
    }

    /**
     * Finds the norm. lambda value <b>along</b> the line. p0 has a norm. lambda of 0, p1 has a lambda of dist(p0, p1).
     * @param ex point x
     * @param ez point z
     * @return the normalized lambda value
     */
    public float getLambdaNorm(float ex, float ez) {
        return (float) (getLambda(ex, ez) * Math.sqrt(dx * dx + dz * dz));
    }

    /**
     * The y value at that point. For points outside the area, the value can be higher than
     * the given y values of p0 and p1.
     * @param ex point x
     * @param ez point z
     * @return the z value at that point (depends on lambda only)
     */
    public float getY(float ex, float ez) {
        return ay + getLambda(ex, ez) * dy;
    }

    /**
     * The y value at that point, clamped to given min/max y values of p0 and p1.
     * @param ex point x
     * @param ez point z
     * @return the y value at that point (depends on lambda only)
     */
    public float getClampedY(float ex, float ez) {
        return ay + TeraMath.clamp(getLambda(ex, ez), 0, 1) * dy;
    }

    /**
     * Finds the signed distance from the line p0, p1 relative to that distance
     * @param ex point x
     * @param ez point z
     * @return the kappa value
     */
    public float getKappa(float ex, float ez) {
        return (ax + getLambda(ex, ez) * dx - ex) / dz;
    }

    /**
     * Finds the signed distance from the line p0, p1 in absolute units
     * @param ex point x
     * @param ez point z
     * @return the norm. kappa value
     */
    public float getKappaNorm(float ex, float ez) {
        return (float) (getKappa(ex, ez) * Math.sqrt(dx * dx + dz * dz));
    }
}
