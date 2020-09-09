// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld;

import org.terasology.math.geom.Vector2i;

import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

/**
 * An interactive test. Use the main method to start.
 */
public final class InteractiveBasicTest {


    private static Point pt;

    private InteractiveBasicTest() {
        // private
    }

    /**
     * @param args (ignored)
     */
    public static void main(String[] args) {
        // Create and set up the window.
        final JFrame frame = new JFrame("Symmetry Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent comp = new JComponent() {
            private static final long serialVersionUID = -3019274194814342555L;

            @Override
            protected void paintComponent(final Graphics g1) {
                super.paintComponent(g1);
                final Graphics2D g = (Graphics2D) g1;
                int scale = 16;
                int centerX = getWidth() / (2 * scale);
                int centerY = getHeight() / (2 * scale);
                g.scale(scale, scale);

                if (pt != null) {
                    Vector2i mp = new Vector2i(pt.x / scale, pt.y / scale);

                    double mouseX = mp.getX() + 0.5;
                    double mouseY = mp.getY() + 0.5;

                    g.setColor(Color.CYAN);
                    g.draw(new Line2D.Double(mouseX, mouseY, mouseX, mouseY));
                }

                // draw grid
                g.setStroke(new BasicStroke(0f));
                g.setColor(Color.LIGHT_GRAY);
                for (int i = 0; i < getWidth() / scale + 1; i++) {
                    g.drawLine(i, 0, i, getHeight());
                }
                for (int i = 0; i < getHeight() / scale + 1; i++) {
                    g.drawLine(0, i, getWidth(), i);
                }

                g.setColor(Color.BLACK);
                g.drawLine(centerX, 0, centerX, getHeight());
                g.drawLine(0, centerY, getWidth(), centerY);

                g.setStroke(new BasicStroke());
            }
        };

        frame.add(comp);

        comp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                pt = null;
                e.getComponent().repaint();
            }
        });

        comp.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseMoved(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                pt = new Point(e.getX(), e.getY());
                e.getComponent().repaint();
            }

        });

        frame.setLocation(500, 200);
        frame.setSize(600, 400);
        frame.setVisible(true);
    }
}
