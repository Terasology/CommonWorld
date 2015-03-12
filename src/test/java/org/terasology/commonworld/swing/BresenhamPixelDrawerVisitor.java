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
package org.terasology.commonworld.swing;

import java.awt.Color;
import org.terasology.commonworld.geom.BresenhamVisitor;

/**
 * Testing the Bresenham line drawing algorithm by 'visual proof'.
 */
public class BresenhamPixelDrawerVisitor implements BresenhamVisitor {

    private PixelDrawer pixelDrawer;
    private Color color;

    public BresenhamPixelDrawerVisitor(PixelDrawer pixelDrawer) {
        this(pixelDrawer, Color.black);
    }

    public BresenhamPixelDrawerVisitor(PixelDrawer pixelDrawer, Color color) {
        this.pixelDrawer = pixelDrawer;
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void visit(int x, int y) {
        pixelDrawer.drawPixel(x, y, color);
    }
}
