package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.objects.nonmovable.Barrel;
import org.freeware.monakhov.game3d.objects.movable.Hero;
import org.freeware.monakhov.game3d.objects.nonmovable.Tree;
import org.freeware.monakhov.game3d.objects.nonmovable.Key;
import org.freeware.monakhov.game3d.objects.nonmovable.Lamp;
import org.freeware.monakhov.game3d.objects.nonmovable.Milton;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.xml.parsers.ParserConfigurationException;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.map.XMLWorldLoader;
import org.freeware.monakhov.game3d.objects.nonmovable.Fire;
import org.xml.sax.SAXException;

public class MainFrame extends javax.swing.JFrame {

    GraphicsEngine engine;
    World world;
    Screen screen;
    Hero hero;

    public MainFrame() throws ParserConfigurationException, SAXException, IOException {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(false);
        setUndecorated(true);
        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setSize(new Dimension(rect.width, rect.height));
        screen = new Screen(rect.width, rect.height);
        world = new World();
        hero = new Hero(new Point(256, 256), world);
        engine = new GraphicsEngine(world, hero, screen);
        XMLWorldLoader loader = new XMLWorldLoader();
        loader.parse(world, MainFrame.class.getResourceAsStream("/org/freeware/monakhov/game3d/map/testWorld1.xml"));
        hero.setRoom(world.getRoom("r0"));
        world.addObject("01", new Barrel(new Point(128, 896)));
        world.addObject("011", new Fire(new Point(128, 128)));
        world.addObject("012", new Fire(new Point(256, 128)));
        world.addObject("013", new Fire(new Point(384, 128)));
        world.addObject("014", new Fire(new Point(512, 128)));        
        world.addObject("02", new Milton(new Point(640, 896)));
        world.addObject("03", new Tree(new Point(2048, 512)));
        world.addObject("04", new Lamp(new Point(128, 896)));
        world.addObject("05", new Key(new Point(512, 512)));
        world.addObject("06", new Fire(new Point(128, 512)));
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyDispatcher());
        Timer sec = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.printf("%d FPS%n", frames);
                frames = 0;
            }
        });
        sec.start();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long now = System.nanoTime();
                    analyseKeys();
                    try {
                        engine.doCycle();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    frames++;
                    frameNanoTime = System.nanoTime() - now;                    
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            repaint();
                        }
                    });
                }
            }
        });
        t.start();
    }

    private volatile int frames;

    @Override
    public void paint(Graphics g) {
        Rectangle rr = this.getBounds();
        screen.paint(g, 0, 0, rr.width, rr.height);
    }

    private long frameNanoTime;

    boolean left;
    boolean right;
    boolean forward;
    boolean backward;
    boolean strifeLeft;
    boolean strifeRight;

    final static double TURN_SPEED = Math.PI / 1000000000.0;
    final static double MOVE_SPEED = 1024 / 1000000000.0;

    void analyseKeys() {
        double ts = TURN_SPEED * frameNanoTime;
        double ms = MOVE_SPEED * frameNanoTime;
        if (left) {
            hero.setAzimuth(hero.getAzimuth() - ts);
        }
        if (right) {
            hero.setAzimuth(hero.getAzimuth() + ts);
        }
        if (forward) {
            hero.moveBy(ms, 0);
        }
        if (backward) {
            hero.moveBy(-ms, 0);
        }
        if (strifeLeft) {
            hero.moveBy(0, -ms);
        }
        if (strifeRight) {
            hero.moveBy(0, ms);
        }
    }

    private class KeyDispatcher implements KeyEventDispatcher {

        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                    case KeyEvent.VK_LEFT:
                        left = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        right = true;
                        break;
                    case KeyEvent.VK_UP:
                        forward = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        backward = true;
                        break;
                    case KeyEvent.VK_Z:
                        strifeLeft = true;
                        break;
                    case KeyEvent.VK_X:
                        strifeRight = true;
                        break;
                    case KeyEvent.VK_TAB:
                        engine.toggleMap();
                        break;
                }
            }
            if (e.getID() == KeyEvent.KEY_RELEASED) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        left = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        right = false;
                        break;
                    case KeyEvent.VK_UP:
                        forward = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        backward = false;
                        break;
                    case KeyEvent.VK_Z:
                        strifeLeft = false;
                        break;
                    case KeyEvent.VK_X:
                        strifeRight = false;
                        break;
                }
            }
            return false;
        }
    }
}
