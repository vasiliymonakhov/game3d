/**
 * This software is free. You can use it without any limitations, but I don't
 * give any kind of warranties!
 */
package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.objects.movable.Hero;
import org.freeware.monakhov.game3d.objects.WorldObject;
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
import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;

/**
 * This is a Graphics Engine
 *
 * @author Vasily Monakhov
 */
public class GraphicsEngine {

    private final World world;

    private final Hero hero;

    private final Screen screen;

    private final double farFarFrontier;

    private final Line[] mapLines;
    private final Point[] rayPoints;
    private final Point[] transformedRayPoints;
    private final Point[] wallsIntersectPoints;
    private final WallColumnDrawer[] wallColumnDrawers;
    private final SpriteColumnDrawer[] spriteColumnDrawers;

    GraphicsEngine(World world, Hero hero, Screen screen) {
        this.world = world;
        this.hero = hero;
        this.screen = screen;
        mapLines = new Line[screen.getWidth()];
        farFarFrontier = KORRECTION * screen.getWidth() / 2;
        rayPoints = new Point[screen.getWidth()];
        int index1 = screen.getWidth() / 2 - 1;
        int index2 = index1 + 1;
        int steps = index2;
        for (int i = 0; i < steps; i++) {
            rayPoints[index1 - i] = new Point(-0.5 - i * KORRECTION, farFarFrontier);
            rayPoints[index2 + i] = new Point(0.5 + i * KORRECTION, farFarFrontier);
        }
        transformedRayPoints = new Point[screen.getWidth()];
        wallsIntersectPoints = new Point[screen.getWidth()];
        for (int i = 0; i < screen.getWidth(); i++) {
            wallsIntersectPoints[i] = new Point();
            transformedRayPoints[i] = new Point();
        }
        wallColumnDrawers = new WallColumnDrawer[screen.getWidth()];
        spriteColumnDrawers = new SpriteColumnDrawer[screen.getWidth()];
        for (int i = 0; i < screen.getWidth(); i++) {
            wallColumnDrawers[i] = new WallColumnDrawer(i);
            spriteColumnDrawers[i] = new SpriteColumnDrawer(i);
        }
    }

    void clearRender() {
        Arrays.fill(mapLines, null);
    }

    void checkVisibleRooms() {
        hero.getRoom().checkVisibility(mapLines, hero.getPosition(), transformedRayPoints, wallsIntersectPoints);
    }

    public final static double WALL_SIZE = 256;
    public final static double KORRECTION = 64;

    private final static Executor EXECUTOR = Executors.newFixedThreadPool(16);

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
                double dist = SpecialMath.lineLength(hero.getPosition(), wallsIntersectPoints[index]);
                double k = SpecialMath.lineLength(hero.getPosition(), transformedRayPoints[index]);
                double h = WALL_SIZE * k / (dist * KORRECTION);
                int ch = (int) Math.round((screen.getHeight() - h) / 2);
                g.drawImage(l.getSubImage(wallsIntersectPoints[index]), index, ch, 1, (int) Math.round(h), null);
            }
            doneSignal.countDown();
        }
    }

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

    private final ArrayList<WorldObject> objectsSortList = new ArrayList<>();

    Comparator<WorldObject> objectsSortComparator = new Comparator<WorldObject>() {
        @Override
        public int compare(WorldObject o1, WorldObject o2) {
            return (int) (o2.distanceTo(hero.getPosition()) - o1.distanceTo(hero.getPosition()));
        }
    };

    private class SpriteColumnDrawer implements Runnable {

        SpriteColumnDrawer(int index) {
            this.index = index;
        }

        void set(Graphics2D g, Point s, Point e, Sprite sprite, CountDownLatch doneSignal) {
            this.g = g;
            this.s = s;
            this.e = e;
            this.sprite = sprite;
            this.doneSignal = doneSignal;
        }

        private CountDownLatch doneSignal;
        private Graphics2D g;
        private final int index;
        private final Point p = new Point();
        private Point s;
        private Point e;
        private Sprite sprite;

        @Override
        public void run() {
            if (SpecialMath.lineIntersection(s, e, hero.getPosition(), wallsIntersectPoints[index], p)) {
                if (p.between(s, e) && p.between(hero.getPosition(), wallsIntersectPoints[index])) {
                    double dist = SpecialMath.lineLength(hero.getPosition(), p);
                    double k = SpecialMath.lineLength(hero.getPosition(), transformedRayPoints[index]);
                    double h = GraphicsEngine.WALL_SIZE * k / (dist * GraphicsEngine.KORRECTION);
                    double sh = h * sprite.getHeight() / GraphicsEngine.WALL_SIZE;
                    double syo = h * sprite.getYOffset() / GraphicsEngine.WALL_SIZE;
                    double ch = (int) Math.round((screen.getHeight() - h) / 2);
                    long xOffset = Math.round(SpecialMath.lineLength(s, p));
                    int spriteXOffset = (int) (xOffset % sprite.getWidth());
                    g.drawImage(sprite.getSubImage(spriteXOffset, 0), index, (int) (ch + syo), 1, (int) sh, null);
                }
            }
            doneSignal.countDown();
        }
    }

    void checkAndRenderVisibleObjects() throws InterruptedException {
        Graphics2D g = (Graphics2D) screen.getImage().getGraphics();
        //g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        //g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        objectsSortList.clear();
        objectsSortList.addAll(world.getAllObjects());
        Collections.sort(objectsSortList, objectsSortComparator);
        for (WorldObject wobj : objectsSortList) {
            Point s = new Point();
            Point e = new Point();
            Sprite sprite = wobj.getSprite();
            int sw2 = sprite.getWidth() / 2;
            double deltaX = sw2 * Math.cos(-hero.getAzimuth());
            double deltaY = sw2 * Math.sin(-hero.getAzimuth());
            Point op = wobj.getPosition();
            s.moveTo(op.getX() - deltaX, op.getY() - deltaY);
            e.moveTo(op.getX() + deltaX, op.getY() + deltaY);
            CountDownLatch doneSignal = new CountDownLatch(mapLines.length);
            for (int i = 0; i < transformedRayPoints.length; i++) {
                spriteColumnDrawers[i].set(g, s, e, sprite, doneSignal);
                EXECUTOR.execute(spriteColumnDrawers[i]);
            }
            doneSignal.await();
        }
    }

    private boolean mapEnabled;

    void toggleMap() {
        mapEnabled = !mapEnabled;
    }

    void render() throws InterruptedException {
        renderFloor();
        renderWalls();
        checkAndRenderVisibleObjects();
        if (mapEnabled) {
            drawMap();
        }
    }

    void transform() {
        double sin = Math.sin(-hero.getAzimuth());
        double cos = Math.cos(-hero.getAzimuth());
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
                if (!l.isEverSeen()) {
                    continue;
                }
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
