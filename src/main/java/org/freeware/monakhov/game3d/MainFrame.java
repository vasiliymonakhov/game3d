/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.freeware.monakhov.game3d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import javax.xml.parsers.ParserConfigurationException;
import org.freeware.monakhov.game3d.maps.Point;
import org.freeware.monakhov.game3d.maps.TextureManager;
import org.freeware.monakhov.game3d.maps.World;
import org.freeware.monakhov.game3d.maps.XMLWorldLoader;
import org.xml.sax.SAXException;

/**
 *
 * @author vasya
 */
public class MainFrame extends javax.swing.JFrame {

    GraphicsEngine engine;
    World world;
    Screen screen;
    Hero hero;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() throws ParserConfigurationException, SAXException, IOException {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(false);
        setUndecorated(true);
        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setSize(new Dimension(rect.width, rect.height));
        
        screen = new Screen(rect.width / 1, rect.height / 1);

        hero = new Hero(new Point(10, 10));
        world = new World();
        engine = new GraphicsEngine(world, hero, screen);
        XMLWorldLoader loader = new XMLWorldLoader();
        TextureManager textureManager = new TextureManager();
        textureManager.add("brick01", "/org/freeware/monakhov/game3d/maps/brick01.jpg");
        textureManager.add("brick02", "/org/freeware/monakhov/game3d/maps/brick02.jpg");
        textureManager.add("brick03", "/org/freeware/monakhov/game3d/maps/brick03.jpg");
        loader.parse(world, MainFrame.class.getResourceAsStream("/org/freeware/monakhov/game3d/maps/testWorld1.xml"), textureManager);
        hero.setRoom(world.getRoom("r0"));
        world.addObject("01", new StaticObject(new Point(5, 35), "green_barrel"));
        world.addObject("02", new StaticObject(new Point(25, 35), "milton"));
        world.addObject("03", new StaticObject(new Point(80, 20), "tree"));
        
        addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyDispatcher());
        Timer repainter = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        repainter.setCoalesce(true);
        repainter.start();
    }

    @Override
    public void paint(Graphics g) {
        long now = System.nanoTime();
        try {
            analyseKeys();
            engine.doCycle();
            Rectangle rr = this.getBounds();
            screen.paint(g, 0, 0, rr.width, rr.height);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        frameNanoTime = System.nanoTime() - now;
    }

    private long frameNanoTime;
    
    boolean left;
    boolean right;
    boolean forward;
    boolean backward;
    boolean strifeLeft;
    boolean strifeRight;

    final static double TURN_SPEED = Math.PI / 1000000000.0;
    final static double MOVE_SPEED = 20 / 1000000000.0;
    
    void analyseKeys() {
        double ts = TURN_SPEED * frameNanoTime;
        double ms = MOVE_SPEED * frameNanoTime;
        if (left) {
            hero.setAsimuth(hero.getAsimuth() - ts);
        }
        if (right) {
            hero.setAsimuth(hero.getAsimuth() + ts);
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
