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

import java.util.Collection;
import java.util.HashSet;
import org.terasology.math.geom.Vector2i;

public class BresenhamCollectorVisitor implements BresenhamVisitor {

    private Collection<Vector2i> line;

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
