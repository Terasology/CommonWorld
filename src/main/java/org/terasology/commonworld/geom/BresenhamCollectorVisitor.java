// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.commonworld.geom;

import org.terasology.math.geom.Vector2i;

import java.util.Collection;
import java.util.HashSet;

public class BresenhamCollectorVisitor implements BresenhamVisitor {

    private final Collection<Vector2i> line;

    public BresenhamCollectorVisitor() {
        line = new HashSet<>();
    }

    public Collection<Vector2i> getLinePoints() {
        return line;
    }

    public void clearLinePoints() {
        line.clear();
    }

    @Override
    public void visit(int x, int y) {
        line.add(new Vector2i(x, y));
    }
}
