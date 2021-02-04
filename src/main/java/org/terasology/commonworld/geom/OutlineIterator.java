// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld.geom;

import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.terasology.world.block.BlockArea;
import org.terasology.world.block.BlockAreac;

import java.util.Iterator;

public class OutlineIterator implements Iterable<Vector2ic> {
    private final Vector2i start = new Vector2i();
    private final Direction startDir;
    private final boolean clockwise;
    private final BlockArea rect = new BlockArea(BlockArea.INVALID);

    public OutlineIterator(BlockAreac rect, boolean clockwise) {
        this(rect, clockwise, rect.getMin(new Vector2i()));
    }

    public OutlineIterator(BlockAreac rect, boolean clockwise, Vector2ic start) {
        if (!rect.contains(start)) {
            throw new IllegalArgumentException("start must be on the border of the rectangle");
        }

        Direction dir = null;
        if (start.x() == rect.maxX()) {
            dir = clockwise ? Direction.POS_Y : Direction.NEG_Y;
        }
        if (start.y() == rect.maxY()) {
            dir = clockwise ? Direction.NEG_X : Direction.POS_X;
        }
        if (start.x() == rect.minX()) {
            dir = clockwise ? Direction.NEG_Y : Direction.POS_Y;
        }
        if (start.y() == rect.minY()) {
            if (clockwise && start.x() < rect.maxX()) {
                dir = Direction.POS_X;
            }
            if (!clockwise && start.x() > rect.minX()) {
                dir = Direction.NEG_X;
            }
        }
        if (dir == null) {
            throw new IllegalArgumentException("start must be on the border of the rectangle");
        }

        this.start.set(start);
        this.startDir = dir;
        this.rect.set(rect);
        this.clockwise = clockwise;
    }


    private static enum Direction {
        POS_X,
        POS_Y,
        NEG_X,
        NEG_Y
    }

        @Override
    public Iterator<Vector2ic> iterator() {
            return new Iterator<Vector2ic>() {
                private Vector2i pos = new Vector2i();
                private Direction dir = startDir;
                private int x = start.x();
                private int y = start.y();

                @Override
                public Vector2ic next() {
                    pos.set(x, y);

                    switch (dir) {
                        case POS_X:
                            x++;
                            if (x == rect.maxX()) {
                                dir = clockwise ? Direction.POS_Y : Direction.NEG_Y;
                            }
                            break;
                        case POS_Y:
                            y++;
                            if (y == rect.maxY()) {
                                dir = clockwise ? Direction.NEG_X :Direction.POS_X;
                            }
                            break;
                        case NEG_X:
                            x--;
                            if (x == rect.minX()) {
                                dir = clockwise ? Direction.NEG_Y :Direction.POS_Y;
                            }
                            break;
                        case NEG_Y:
                            y--;
                            if (y == rect.minY()) {
                                dir = clockwise ? Direction.POS_X : Direction.NEG_X;
                            }
                            break;
                    }

                    return pos;
                }

                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException("remove");
                }
            };
    }

    public int length() {
        return 2 * rect.getSizeX() + 2 * rect.getSizeY() - 4; // -4 = 2*(-1) + 2*(-1) for maxX() and maxY()
    }
}
