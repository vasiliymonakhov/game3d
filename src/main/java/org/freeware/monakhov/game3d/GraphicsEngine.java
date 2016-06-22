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
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.freeware.monakhov.game3d.map.Image;
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
    private final FloorCeilingDrawer[] floorCeilingDrawers;
    private final SkyDrawer[] skyDrawers;

    public final static double WALL_SIZE = 256;
    public final static double KORRECTION = 64;

    private final static Executor EXECUTOR = Executors.newFixedThreadPool(4);
    private final ArrayList<WorldObject> objectsSortList = new ArrayList<>();

    BufferedImage sky;

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
        floorCeilingDrawers = new FloorCeilingDrawer[4];
        int w = screen.getWidth() / floorCeilingDrawers.length;
        BufferedImage floor = world.getFloor() != null ? Image.get(world.getFloor()).getImage() : null;
        BufferedImage ceiling = world.getCeiling() != null ? Image.get(world.getCeiling()).getImage() : null;
        for (int i = 0; i < floorCeilingDrawers.length; i++) {
            floorCeilingDrawers[i] = new FloorCeilingDrawer(i * w, w, floor, ceiling);
        }
        if (world.getSky() != null) {
            sky = Image.get(world.getSky()).getImage();
            skyDrawers = new SkyDrawer[4];
            w = screen.getWidth() / skyDrawers.length;
            for (int i = 0; i < skyDrawers.length; i++) {
                skyDrawers[i] = new SkyDrawer(i * w, w, sky);
            }
        } else {
            skyDrawers = null;
        }

    }

    void clearRender() {
        Arrays.fill(mapLines, null);
    }

    private List<Room> visibleRooms = new ArrayList<Room>();
    
    void checkVisibleRooms() {
        visibleRooms.clear();
        hero.getRoom().checkVisibility(mapLines, hero.getPosition(), transformedRayPoints, wallsIntersectPoints, visibleRooms);
    }

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
            try {
                Line l = mapLines[index];
                if (l != null) {
                    double dist = SpecialMath.lineLength(hero.getPosition(), wallsIntersectPoints[index]);
                    double k = SpecialMath.lineLength(hero.getPosition(), transformedRayPoints[index]);
                    double h = WALL_SIZE * k / (dist * KORRECTION);
                    int ch = (int) ((screen.getHeight() - h) / 2);
                    g.drawImage(l.getSubImage(wallsIntersectPoints[index]), index, ch, 1, (int) h, null);
                }
            } finally {
                doneSignal.countDown();
            }
        }
    }

    void renderWalls() throws InterruptedException {
        Graphics2D g = (Graphics2D) screen.getImage().getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        CountDownLatch doneSignal = new CountDownLatch(mapLines.length);
        for (int i = 0; i < mapLines.length; i++) {
            wallColumnDrawers[i].set(g, doneSignal);
            EXECUTOR.execute(wallColumnDrawers[i]);
        }
        doneSignal.await();
    }

    private class FloorCeilingDrawer implements Runnable {

        private final int x;
        private final int w;
        private CountDownLatch doneSignal;
        private Graphics2D g;
        private final BufferedImage floor;
        private final BufferedImage ceiling;

        void set(Graphics2D g, CountDownLatch doneSignal) {
            this.g = g;
            this.doneSignal = doneSignal;
        }

        FloorCeilingDrawer(int x, int w, BufferedImage floor, BufferedImage ceiling) {
            this.x = x;
            this.w = w;
            this.floor = floor;
            this.ceiling = ceiling;
        }

        @Override
        public void run() {
            try {
                int height = screen.getHeight() / 2;
                if (ceiling != null) {
                    g.drawImage(ceiling, x, 0, w, height, null);
                }
                if (floor != null) {
                    g.drawImage(floor, x, height + 1, w, height, null);
                }
            } finally {
                doneSignal.countDown();
            }
        }

    }

    void renderFloor() throws InterruptedException {
        Graphics2D g = (Graphics2D) screen.getImage().getGraphics();
        // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);        
        // g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        CountDownLatch doneSignal = new CountDownLatch(floorCeilingDrawers.length);
        for (FloorCeilingDrawer floorCeilingDrawer : floorCeilingDrawers) {
            floorCeilingDrawer.set(g, doneSignal);
            EXECUTOR.execute(floorCeilingDrawer);
        }
        doneSignal.await();
        if (skyDrawers != null) {
            doneSignal = new CountDownLatch(floorCeilingDrawers.length);
            double pi2 = Math.PI * 2;
            int a = (int) (sky.getWidth() * ((hero.getAzimuth() + pi2) % pi2) / pi2);
            for (SkyDrawer skyDrawer : skyDrawers) {
                skyDrawer.set(g, a, doneSignal);
                EXECUTOR.execute(skyDrawer);
            }
            doneSignal.await();
        }

    }

    private class SkyDrawer implements Runnable {

        private final int x;
        private final int w;
        private int a;
        private CountDownLatch doneSignal;
        private Graphics2D g;
        private final BufferedImage sky;

        void set(Graphics2D g, int a, CountDownLatch doneSignal) {
            this.g = g;
            this.doneSignal = doneSignal;
            this.a = a;
        }

        SkyDrawer(int x, int w, BufferedImage sky) {
            this.x = x;
            this.w = w;
            this.sky = sky;
        }

        @Override
        public void run() {
            try {
                int height = screen.getHeight() / 2;
                int xx = (a + x) % sky.getWidth();
                if (xx + w <= sky.getWidth()) {
                    g.drawImage(sky.getSubimage(xx, 0, w, sky.getHeight()), x, 0, w, height, null);
                } else {
                    int w1 = sky.getWidth() - xx;
                    g.drawImage(sky.getSubimage(xx, 0, w1, sky.getHeight()), x, 0, w1, height, null);
                    int w2 = w - w1;
                    int xx2 = (xx+ w1) % sky.getWidth();
                    g.drawImage(sky.getSubimage(xx2, 0, w2, sky.getHeight()), x + w1, 0, w2, height, null);
                }
            } finally {
                doneSignal.countDown();
            }
        }
    }

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
        public synchronized void run() {
            try {
                if (SpecialMath.lineIntersection(s, e, hero.getPosition(), wallsIntersectPoints[index], p)) {
                    if (p.between(s, e) && p.between(hero.getPosition(), wallsIntersectPoints[index])) {
                        double dist = SpecialMath.lineLength(hero.getPosition(), p);
                        double k = SpecialMath.lineLength(hero.getPosition(), transformedRayPoints[index]);
                        double h = GraphicsEngine.WALL_SIZE * k / (dist * GraphicsEngine.KORRECTION);
                        double sh = h * sprite.getHeight() / GraphicsEngine.WALL_SIZE;
                        double syo = h * sprite.getYOffset() / GraphicsEngine.WALL_SIZE;
                        double ch = (int) ((screen.getHeight() - h) / 2);
                        long xOffset = (long) SpecialMath.lineLength(s, p);
                        int spriteXOffset = (int) (xOffset % sprite.getWidth());
                        g.drawImage(sprite.getSubImage(spriteXOffset, 0), index, (int) (ch + syo), 1, (int) sh, null);
                    }
                }
            } finally {
                doneSignal.countDown();
            }
        }
    }

    void renderObjects() throws InterruptedException {
        Graphics2D g = (Graphics2D) screen.getImage().getGraphics();
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        objectsSortList.clear();
        for (WorldObject wobj : world.getAllObjects()) {
            wobj.turnSpriteToHero(hero);
            boolean flag = false;
            for (Room r : visibleRooms) {
                if (r.insideThisRoom(wobj.getLeft()) || r.insideThisRoom(wobj.getRight())) {
                    flag = true;
                    break;
                }
            }
            if (flag || visibleRooms.contains(wobj.getRoom())) {
                objectsSortList.add(wobj);
            }
        }
        Collections.sort(objectsSortList, objectsSortComparator);
        for (WorldObject wobj : objectsSortList) {
            Sprite sprite = wobj.getSprite();
            CountDownLatch doneSignal = new CountDownLatch(mapLines.length);
            for (int i = 0; i < transformedRayPoints.length; i++) {
                spriteColumnDrawers[i].set(g, wobj.getLeft(), wobj.getRight(), sprite, doneSignal);
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
        renderObjects();
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

    private double mapScale = 0.25;
    
    void incMapScale() {
        mapScale *= 2;
    }
    
    void decMapScale() {
        mapScale /= 2;
    }    
    
    void drawMap() {
        Graphics2D g = (Graphics2D) screen.getImage().getGraphics();
        int dx = screen.getImage().getWidth() / 2;
        int dy = screen.getImage().getHeight() / 2;
        g.setColor(Color.red);
        g.fillOval(dx - 5, dy - 5, 10, 10);
        g.setStroke(WALL);                
        int rx = (int)(20 * Math.sin(hero.getAzimuth()));
        int ry = (int)(20 * Math.cos(hero.getAzimuth()));
        g.drawLine(dx, dy, dx + rx, dy - ry);
        dx += -(int) hero.getPosition().getX() * mapScale;
        dy += (int) hero.getPosition().getY() * mapScale;
        g.setColor(Color.GREEN);
        for (Room r : world.getAllRooms()) {
            for (Line l : r.getAllLines()) {
                if (!l.isEverSeen()) {
                    continue;
                }
                g.drawLine(dx + (int)(l.getStart().getX() * mapScale), dy - (int)(l.getStart().getY() * mapScale),
                        dx + (int)(l.getEnd().getX() * mapScale), dy - (int)(l.getEnd().getY() * mapScale));
            }
        }

    }

}
