package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.objects.movable.Hero;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.xml.parsers.ParserConfigurationException;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.map.XMLResourceLoader;
import org.freeware.monakhov.game3d.map.XMLWorldLoader;
import org.xml.sax.SAXException;

public class MainFrame extends javax.swing.JFrame {

    final GraphicsEngine graphicsEngine;
    final GameEngine gameEngine;
    final World world;
    final Screen screen;
    final Hero hero;

    public MainFrame() throws ParserConfigurationException, SAXException, IOException {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setUndecorated(true);
        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setSize(new Dimension(rect.width, rect.height));

        XMLResourceLoader resLoader = new XMLResourceLoader();
        resLoader.parse(MainFrame.class.getResourceAsStream("/org/freeware/monakhov/game3d/resources.xml"));

        world = new World();
        hero = new Hero(world, new Point());
        world.setHero(hero);
        XMLWorldLoader loader = new XMLWorldLoader();
        loader.parse(world, hero, MainFrame.class.getResourceAsStream("/org/freeware/monakhov/game3d/map/testWorld1.xml"));

        screen = new Screen(rect.width * 4 / 4, rect.height * 4 / 4);
        graphicsEngine = new GraphicsEngine(world, hero, screen);
        gameEngine = new GameEngine(world, hero);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ked);

        Timer sec = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fps = String.format("%d FPS%n", frames);
                frames = 0;
            }
        });
        sec.start();
        final Thread t = new Thread(new Runnable() {

            // int iter;
            @Override
            public void run() {
                while (true) {
                    long now = System.nanoTime();
                    hero.analyseKeys(ked.isLeft(), ked.isRight(), ked.isForward(), ked.isBackward(), ked.isStrafeLeft(), ked.isStrafeRight(), frameNanoTime);
                    if (ked.isInteract()) {
                        gameEngine.interactWithHero();
                        ked.setInteract(false);
                    }
                    gameEngine.doCycle(frameNanoTime);
                    try {
                        graphicsEngine.doCycle();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // if (++iter % 10 == 0) System.gc();
                    frames++;
                    frameNanoTime = System.nanoTime() - now;
                    SwingUtilities.invokeLater(repainter);
                }
            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                t.start();
            }
        });
    }

    final KeyDispatcher ked = new KeyDispatcher();

    private final Runnable repainter = new Runnable() {

        @Override
        public void run() {
            repaint();
        }
    };

    String fps = "";
    Font f = new Font("Arial", 0, 20);

    private volatile int frames;

    private boolean fullScreen = true;

    @Override
    public void paint(Graphics gr) {
        Rectangle rr = this.getBounds();
        Graphics2D g = (Graphics2D) gr;
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (!fullScreen) {
            int x = (rr.width - screen.getWidth()) / 2;
            int y = (rr.height - screen.getHeight()) / 2;
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, rr.width, y);
            g.fillRect(0, rr.height - y, rr.width, y);
            int sw = (rr.width - screen.getWidth()) / 2;
            g.fillRect(0, y, sw, screen.getHeight());
            g.fillRect(x + screen.getWidth(), y, sw, screen.getHeight());
            screen.paint(g, x, y, screen.getWidth(), screen.getHeight());
        } else {
            screen.paint(g, 0, 0, rr.width, rr.height);
        }
        g.setColor(Color.GREEN);
        g.setFont(f);
        g.drawString(fps, 25, 25);
    }

    private long frameNanoTime;

    class KeyDispatcher implements KeyEventDispatcher {

        private boolean strafeLeft;
        private boolean right;
        private boolean forward;
        private boolean backward;
        private boolean strafeRight;
        private boolean left;
        private boolean interact;

        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                    case KeyEvent.VK_LEFT:
                        this.left = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        this.right = true;
                        break;
                    case KeyEvent.VK_UP:
                        this.forward = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        this.backward = true;
                        break;
                    case KeyEvent.VK_Z:
                        this.strafeLeft = true;
                        break;
                    case KeyEvent.VK_X:
                        this.strafeRight = true;
                        break;
                    case KeyEvent.VK_TAB:
                        graphicsEngine.toggleMap();
                        break;
                    case KeyEvent.VK_F12:
                        fullScreen = !fullScreen;
                        break;
                    case KeyEvent.VK_PAGE_UP:
                        graphicsEngine.incMapScale();
                        break;
                    case KeyEvent.VK_PAGE_DOWN:
                        graphicsEngine.decMapScale();
                        break;
                    case KeyEvent.VK_SPACE:
                        interact = true;
                        break;
                }
            }
            if (e.getID() == KeyEvent.KEY_RELEASED) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        this.left = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        this.right = false;
                        break;
                    case KeyEvent.VK_UP:
                        this.forward = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        this.backward = false;
                        break;
                    case KeyEvent.VK_Z:
                        this.strafeLeft = false;
                        break;
                    case KeyEvent.VK_X:
                        this.strafeRight = false;
                        break;
                    case KeyEvent.VK_SPACE:
                        interact = false;
                        break;
                }
            }
            return false;
        }

        /**
         * @return the strafeLeft
         */
        boolean isStrafeLeft() {
            return strafeLeft;
        }

        /**
         * @return the right
         */
        boolean isRight() {
            return right;
        }

        /**
         * @return the forward
         */
        boolean isForward() {
            return forward;
        }

        /**
         * @return the backward
         */
        boolean isBackward() {
            return backward;
        }

        /**
         * @return the strafeRight
         */
        boolean isStrafeRight() {
            return strafeRight;
        }

        /**
         * @return the left
         */
        boolean isLeft() {
            return left;
        }

        /**
         * @return the interact
         */
        boolean isInteract() {
            return interact;
        }

        /**
         * @param interact the interact to set
         */
        void setInteract(boolean interact) {
            this.interact = interact;
        }
    }
}
