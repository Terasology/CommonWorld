// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.commonworld.geom;

import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.Collection;
import java.util.HashSet;

public class BresenhamCollectorVisitor implements BresenhamVisitor {

    private Collection<Vector2ic> line;

    public BresenhamCollectorVisitor() {
        line = new HashSet<>();
    }

    public Collection<Vector2ic> getLinePoints() {
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
