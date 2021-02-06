// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.commonworld.swing;

import org.terasology.commonworld.geom.BresenhamVisitor;

import java.awt.Color;

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
