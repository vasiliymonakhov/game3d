/**
 * This software is free. You can use it without any limitations, but I don't
 * give any kind of warranties!
 */
package org.freeware.monakhov.game3d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.freeware.monakhov.game3d.maps.Line;
import org.freeware.monakhov.game3d.maps.Point;
import org.freeware.monakhov.game3d.maps.Room;
import org.freeware.monakhov.game3d.maps.World;

/**
 * This is a Graphics Engine
 *
 * @author Vasily Monakhov
 */
public class GraphicsEngine {

    private final World world;

    private final Hero hero;

    private final Screen screen;

    private final double perspective;

    private final Line[] mapLines;
    private final Point[] rayPoints;
    private final Point[] transformedRayPoints;
    private final Point[] intersectPoints;

    GraphicsEngine(World world, Hero hero, Screen screen) {
        this.world = world;
        this.hero = hero;
        this.screen = screen;
        mapLines = new Line[screen.getWidth()];
        perspective = KORRECTION * screen.getWidth() / 2;
        rayPoints = new Point[screen.getWidth()];
        int index1 = screen.getWidth() / 2 - 1;
        int index2 = index1 + 1;
        int steps = index2;
        for (int i = 0; i < steps; i++) {
            rayPoints[index1 - i] = new Point(-0.5 - i * KORRECTION, perspective);
            rayPoints[index2 + i] = new Point(0.5 + i * KORRECTION, perspective);
        }
        transformedRayPoints = new Point[screen.getWidth()];
        intersectPoints = new Point[screen.getWidth()];
        for (int i = 0; i < screen.getWidth(); i++) {
            intersectPoints[i] = new Point();
            transformedRayPoints[i] = new Point();
        }
        wallColumnDrawers = new WallColumnDrawer[screen.getWidth()];
        for (int i = 0; i < screen.getWidth(); i++) {
            wallColumnDrawers[i] = new WallColumnDrawer(i);
        }
    }

    void clearRender() {
        Arrays.fill(mapLines, null);
    }

    void checkVisibleRooms() {
        hero.getRoom().checkVisibility(mapLines, hero.getPosition(), transformedRayPoints, intersectPoints);
    }

    final static double WALL_HEIGHT = 256;
    final static double KORRECTION = 64;

    private final static Executor EXECUTOR = Executors.newCachedThreadPool();

    private class WallColumnDrawer implements Runnable {

        WallColumnDrawer(int index) {
            this.index = index;
        }

        void set(Graphics2D g, CountDownLatch doneSignal) {
            this.g = g;
            this.doneSignal = doneSignal;
        }

        private CountDownLatch doneSignal;
        private Graphics2D g;
        private final int index;

        @Override
        public void run() {
            Line l = mapLines[index];
            if (l != null) {
                double dist = SpecialMath.lineLength(hero.getPosition(), intersectPoints[index]);
                double k = SpecialMath.lineLength(hero.getPosition(), transformedRayPoints[index]);
                double h = WALL_HEIGHT * k / (dist * KORRECTION);
                int ch = (int) Math.round((screen.getHeight() - h) / 2);
                g.drawImage(l.getSubImage(intersectPoints[index]), index, ch, 1, (int) Math.round(h), null);
            }
            doneSignal.countDown();
        }
    }

    private final WallColumnDrawer[] wallColumnDrawers;

    void renderWalls() throws InterruptedException {
        Graphics2D g = (Graphics2D) screen.getImage().getGraphics();
        //g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        CountDownLatch doneSignal = new CountDownLatch(mapLines.length);
        for (int i = 0; i < mapLines.length; i++) {
            wallColumnDrawers[i].set(g, doneSignal);
            EXECUTOR.execute(wallColumnDrawers[i]);
        }
        doneSignal.await();
    }

    void renderFloor() {
        Graphics2D g = (Graphics2D) screen.getImage().getGraphics();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, screen.getWidth(), screen.getHeight() / 2);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, screen.getHeight() / 2 + 1, screen.getWidth(), screen.getHeight() / 2);
    }

    void renderObjects() {
        Graphics2D g = (Graphics2D) screen.getImage().getGraphics();
        //g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        //g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        ArrayList<WorldObject> list = new ArrayList<>();
        list.addAll(world.getAllObjects());
        Collections.sort(list, new Comparator<WorldObject>() {
            @Override
            public int compare(WorldObject o1, WorldObject o2) {
                return (int)(o2.distanceTo(hero.getPosition()) - o1.distanceTo(hero.getPosition()));
            }
        });
        for (WorldObject o : list) {
            o.render(g, screen.getHeight(), hero, transformedRayPoints, intersectPoints);
        }
    }
    
    private boolean mapEnabled;

    void toggleMap() {
        mapEnabled = !mapEnabled;
    }

    void render() throws InterruptedException {
        renderFloor();
        renderWalls();
        renderObjects();
        if (mapEnabled) {
            drawMap();
        }
    }

    void transform() {
        double sin = Math.sin(-hero.getAsimuth());
        double cos = Math.cos(-hero.getAsimuth());
        for (int i = 0; i < screen.getWidth(); i++) {
            double x = rayPoints[i].getX();
            double y = rayPoints[i].getY();
            double new_x = x * cos - y * sin + hero.getPosition().getX();
            double new_y = y * cos + x * sin + hero.getPosition().getY();
            transformedRayPoints[i].moveTo(new_x, new_y);
        }
    }
    
    void doCycle() throws InterruptedException {
        world.prepareForVisibilityCheck();
        clearRender();
        transform();
        checkVisibleRooms();
        render();
        screen.swapBuffers();
    }

    BasicStroke LINE = new BasicStroke(1);
    BasicStroke WALL = new BasicStroke(3);

    void drawMap() {
        Graphics2D g = (Graphics2D) screen.getImage().getGraphics();
        int dx = screen.getImage().getWidth() / 2;
        int dy = screen.getImage().getHeight() / 2;
        g.setColor(Color.red);
        g.fillOval(dx - 2, dy - 2, 4, 4);
        dx += -(int) hero.getPosition().getX() / 4;
        dy += (int) hero.getPosition().getY() / 4;
        g.setColor(Color.GREEN);
        for (Room r : world.getAllRooms()) {
            for (Line l : r.getAllLines()) {
                if (!l.isEverSeen()) continue;
                if (l.isVisible()) {
                    g.setStroke(WALL);
                } else {
                    g.setStroke(LINE);
                }
                g.drawLine(dx + (int) l.getStart().getX() / 4, dy - (int) l.getStart().getY() / 4,
                        dx + (int) l.getEnd().getX() / 4, dy - (int) l.getEnd().getY() / 4);
            }
        }

    }
    

}
