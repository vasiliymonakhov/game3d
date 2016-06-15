/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.freeware.monakhov.game3d;

import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
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
        setAlwaysOnTop(true);
        setUndecorated(true);

        setSize(new java.awt.Dimension(1024, 768));
        screen = new Screen(1024, 768);

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
    }

    @Override
    public void paint(Graphics g) {
        engine.doCycle();
        Rectangle rr = this.getBounds();
        int sx = (rr.width - screen.getWidth()) / 2;
        int sy = (rr.height - screen.getHeight()) / 2;
        screen.paint(g, sx, sy);
    }

    private class KeyDispatcher implements KeyEventDispatcher {

        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                    case KeyEvent.VK_LEFT:
                        hero.setAsimuth(hero.getAsimuth() - Math.PI / 16);
                        break;
                    case KeyEvent.VK_RIGHT:
                        hero.setAsimuth(hero.getAsimuth() + Math.PI / 16);
                        break;
                    case KeyEvent.VK_UP:
                        hero.moveBy(1, 0);
                        break;
                    case KeyEvent.VK_DOWN:
                        hero.moveBy(-1, 0);
                        break;
                    case KeyEvent.VK_Z:
                        hero.moveBy(0, -1);
                        break;
                    case KeyEvent.VK_X:
                        hero.moveBy(0, 1);
                        break;
                    case KeyEvent.VK_TAB:
                        engine.toggleMap();
                        break;
                }
            }
            repaint();
            return false;
        }
    }
}
