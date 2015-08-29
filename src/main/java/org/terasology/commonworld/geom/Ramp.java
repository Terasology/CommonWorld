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

import org.terasology.math.geom.BaseVector3f;
import org.terasology.math.geom.BaseVector3i;

/**
 * A plane that is defined by two points.
 * The missing third point is derived by a horizontal (dz = 0) line.
 * The result is a ramp.
 * <pre>
 *  ( ex )   ( ax )     ( dx )     ( -dy )
 *  ( ey ) = ( ay ) + L ( dy ) + K (  dx )
 *  ( ez )   ( az )     ( dz )     (  0  )
 *
 *  ex = ax + L*dx - K*dy
 *
 *        ax + L*dx - ex
 *  K = ------------------
 *             dy
 *
 *
 *  ey = ay + L*dy + K*dx
 *
 *        (ey*dy - ay*dy - ax*dx + ex*dx)
 *  L = -----------------------------------
 *                  (dy^2 + dx^2)
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
    public Ramp(BaseVector3f p0, BaseVector3f p1) {
        ax = p0.getX();
        ay = p0.getY();
        az = p0.getZ();
        dx = p1.getX() - p0.getX();
        dy = p1.getY() - p0.getY();
        dz = p1.getZ() - p0.getZ();
    }

    /**
     * @param p0 the first point
     * @param p1 the second point
     */
    public Ramp(BaseVector3i p0, BaseVector3i p1) {
        ax = p0.getX();
        ay = p0.getY();
        az = p0.getZ();
        dx = p1.getX() - p0.getX();
        dy = p1.getY() - p0.getY();
        dz = p1.getZ() - p0.getZ();
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
     * @param ey point y
     * @return the lambda value
     */
    public float getLambda(float ex, float ey) {
        return (ey * dy - ay * dy - ax * dx + ex * dx) / (dy * dy + dx * dx);
    }

    /**
     * Finds the norm. lambda value <b>along</b> the line. p0 has a norm. lambda of 0, p1 has a lambda of dist(p0, p1).
     * @param ex point x
     * @param ey point y
     * @return the normalized lambda value
     */
    public float getLambdaNorm(float ex, float ey) {
        return (float) (getLambda(ex, ey) * Math.sqrt(dx * dx + dy * dy));
    }

    /**
     * @param ex point x
     * @param ey point y
     * @return the z value at that point (depends on lambda only)
     */
    public float getZ(float ex, float ey) {
        return az + getLambda(ex, ey) * dz;
    }

    /**
     * Finds the distance from the line p0, p1 relative to that distance
     * @param ex point x
     * @param ey point y
     * @return the kappa value
     */
    public float getKappa(float ex, float ey) {
        return (ax + getLambda(ex, ey) * dx - ex) / dy;
    }

    /**
     * Finds the distance from the line p0, p1 in absolute units
     * @param ex point x
     * @param ey point y
     * @return the norm. kappa value
     */
    public float getKappaNorm(float ex, float ey) {
        return (float) (getKappa(ex, ey) * Math.sqrt(dx * dx + dy * dy));
    }
}
