package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.objects.movable.Hero;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.map.XMLResourceLoader;
import org.freeware.monakhov.game3d.map.XMLWorldLoader;
import org.xml.sax.SAXException;

public class MainFrame extends javax.swing.JFrame {

    ScreenBuffer screen;
    GraphicsEngine graphicsEngine;

    final GameEngine gameEngine;
    final World world;
    final Hero hero;

    public MainFrame() throws ParserConfigurationException, SAXException, IOException {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setUndecorated(true);
        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setSize(new Dimension(rect.width, rect.height));

        XMLResourceLoader resLoader = new XMLResourceLoader();
        try (InputStream is = MainFrame.class.getResourceAsStream("/org/freeware/monakhov/game3d/resources.xml")) {
            resLoader.parse(is);
        }

        world = new World();
        hero = new Hero(world, new Point());
        world.setHero(hero);
        XMLWorldLoader loader = new XMLWorldLoader();
        try (InputStream is = MainFrame.class.getResourceAsStream("/org/freeware/monakhov/game3d/map/testWorld1.xml")) {
            loader.parse(world, is);
        }

        makeScreenAndEngine();

        gameEngine = new GameEngine(world);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ked);

        final Thread t = new Thread(new Runnable() {

            // int iter;
            @Override
            public void run() {
                while (true) {
                    try {
                        long now = System.nanoTime();
                        hero.analyseKeys(ked.isLeft(), ked.isRight(), ked.isForward(), ked.isBackward(), ked.isStrafeLeft(), ked.isStrafeRight(), frameNanoTime);
                        if (ked.isInteract()) {
                            gameEngine.interactWithHero();
                            ked.setInteract(false);
                        }
                        gameEngine.doCycle(frameNanoTime);
                        boolean remakeEngine = false;
                        if (ked.isIncPercent() && percent < 100) {
                            remakeEngine = true;
                            percent += 5;
                        }
                        if (ked.isDecPercent() && percent >= 20) {
                            remakeEngine = true;
                            percent -= 5;
                        }
                        if (remakeEngine) {
                            makeScreenAndEngine();
                        }
                        graphicsEngine.doCycle();
                        // if (++iter % 10 == 0) System.gc();
                        frameNanoTime = System.nanoTime() - now;
                        SwingUtilities.invokeLater(repainter);
                    } catch (Throwable ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
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

    int percent = 100;

    private void makeScreenAndEngine() {
        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        screen = new ScreenBuffer(rect.width * percent / 100, rect.height * percent / 100);
        graphicsEngine = new GraphicsEngine(world, screen);
    }

    final KeyDispatcher ked = new KeyDispatcher();

    private final Runnable repainter = new Runnable() {

        @Override
        public void run() {
            repaint();
        }
    };

    private boolean fullScreen = false;

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
            g.fillRect(0, screen.getHeight() + y, rr.width, y + 1);
            int sw = (rr.width - screen.getWidth()) / 2;
            g.fillRect(0, y, sw, screen.getHeight());
            g.fillRect(x + screen.getWidth(), y, sw, screen.getHeight());
            screen.paint(g, x, y, screen.getWidth(), screen.getHeight());
        } else {
            screen.paint(g, 0, 0, rr.width, rr.height);
        }
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
        private boolean incPercent;
        private boolean decPercent;

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
                    case KeyEvent.VK_F8:
                        screen.makeScreenShot();
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
                    case KeyEvent.VK_F6:
                        incPercent = true;
                        break;
                    case KeyEvent.VK_F5:
                        decPercent = true;
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

        /**
         * @return the incPercent
         */
        boolean isIncPercent() {
            boolean res = incPercent;
            incPercent = false;
            return res;
        }

        /**
         * @return the decPercent
         */
        boolean isDecPercent() {
            boolean res = decPercent;
            decPercent = false;
            return res;
        }
    }
}
