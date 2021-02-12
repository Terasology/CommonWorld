// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.commonworld.contour;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.commonworld.heightmap.ConvertingHeightMap;
import org.terasology.commonworld.heightmap.HeightMaps;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests {@link ContourTracer}
 */
public class ContourTracingTest {

    private static final Logger logger = LoggerFactory.getLogger(ContourTracingTest.class);

    /**
     * Tests contour tracing
     */
    @Test
    public void test() {

        List<String> data = Arrays.asList(
            "             ",
            "    XXXXXX   ",
            "  XXXXXXXXXX ",
            " XXX   XX    ",
            "   XXXXXXX   ",
            "  XXXXXXXX   ",
            "    XXXX     ",
            "   XXXX      ",
            "   XXX       ",
            "             ");

        int width = data.get(0).length();
        int height = data.size();

        Rectangle rc = new Rectangle(0, 0, width, height);
        ConvertingHeightMap hm = new ConvertingHeightMap(HeightMaps.stringBased(data));
        hm.addConversion(Integer.valueOf('X'), 0);
        hm.addConversion(Integer.valueOf(' '), 10);

        ContourTracer ct = new ContourTracer(hm, rc, 5);
        List<Contour> all = Lists.newArrayList();
        all.addAll(ct.getOuterContours());
        all.addAll(ct.getInnerContours());
        drawContour(all, width, height);

        List<String> desired = Arrays.asList(
            "-------------",
            "----XXXXXX---",
            "--XXOOO--XXX-",
            "-XXO---OX----",
            "---XOOO--X---",
            "--XX----XX---",
            "----X--X-----",
            "---X--X------",
            "---XXX-------",
            "-------------");

        List<String> reality = drawContour(all, width, height);

        for (int line = 0; line < height; line++) {
            assertEquals("Line " + line, desired.get(line), reality.get(line));
        }
    }

    /**
     * Some simple tests on curve simplification
     */
    @Test
    public void testSimplification() {
        Contour pts = new Contour();

        for (int i = 3; i < 5; i++) {
            Point p1 = new Point(i, 0);
            pts.addPoint(p1);
        }
        for (int i = 0; i < 5; i++) {
            Point p1 = new Point(5 + i, i);
            pts.addPoint(p1);
        }

        for (int i = 0; i < 5; i++) {
            Point p1 = new Point(10, i + 5);
            pts.addPoint(p1);
        }

        for (int i = 0; i < 5; i++) {
            Point p1 = new Point(10 - i, 10 - i);
            pts.addPoint(p1);
        }

        for (int i = 0; i < 5; i++) {
            Point p1 = new Point(5 - i, 5);
            pts.addPoint(p1);
        }

        for (int i = 0; i < 5; i++) {
            Point p1 = new Point(0, 5 - i);
            pts.addPoint(p1);
        }

        for (int i = 0; i < 3; i++) {
            Point p1 = new Point(i, 0);
            pts.addPoint(p1);
        }
        Collection<Point> simplePts = pts.getSimplifiedCurve();

        char[][] data = new char[11][11];
        for (int i = 0; i < data.length; i++) {
            Arrays.fill(data[i], ' ');
        }

        for (Point p : pts.getPoints()) {
            data[p.y][p.x] = 'O';
        }


        for (Point p : simplePts) {
            data[p.y][p.x] = 'X';
        }

        for (char[] line : data) {
            logger.info(new String(line));
        }

        List<Point> shouldBe = ImmutableList.of(
                new Point(5, 0),
                new Point(10, 5),
                new Point(10, 10),
                new Point(5, 5),
                new Point(0, 5),
                new Point(0, 0));

        assertEquals(shouldBe, simplePts);
    }

    private List<String> drawContour(Collection<Contour> cts, int width, int height) {

        char[] glyphs = {'X', 'O', 'H', 'V', 'S', 'I'};

        List<String> result = Lists.newArrayList();
        String current = "";

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Point p = new Point(x, y);
                char contId = '-';
                int idx = 0;

                for (Contour c : cts) {
                    if (c.getPoints().contains(p)) {
                        contId = glyphs[idx];
                        break;
                    }
                    idx++;

                    if (idx >= glyphs.length) {
                        idx = 0;
                    }
                }

                current += contId;
            }

            result.add(current);
            current = "";
        }

        return result;
    }
}
