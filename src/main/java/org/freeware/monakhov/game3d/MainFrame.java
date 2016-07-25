package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.objects.movable.Hero;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.resources.XMLResourceLoader;
import org.freeware.monakhov.game3d.map.XMLWorldLoader;
import org.xml.sax.SAXException;

public class MainFrame extends javax.swing.JFrame {

    ScreenBuffer screen;
    GraphicsEngine graphicsEngine;

    final GameEngine gameEngine;
    final World world;
    final Hero hero;
    private final Semaphore semaphore = new Semaphore(1);


    public MainFrame() throws ParserConfigurationException, SAXException, IOException, FontFormatException {
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
        try (InputStream is = MainFrame.class.getResourceAsStream("/org/freeware/monakhov/game3d/map/testWorld3.xml")) {
            loader.parse(world, is);
        }

        makeScreenAndEngine();

        gameEngine = new GameEngine(world, semaphore);

        ked = new KeyDispatcher(this, graphicsEngine, screen);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ked);

        final Thread graphicsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        graphicsEngine.doCycle();
                        SwingUtilities.invokeLater(repainter);
                    } catch (Throwable ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        graphicsThread.setPriority(Thread.MAX_PRIORITY);

        final Thread gameThread = new Thread(new Runnable() {

            private long frameNanoTime;

            @Override
            public void run() {
                while (true) {
                    try {
                        long now = System.nanoTime();
                        gameEngine.doCycle(frameNanoTime, ked);
                        boolean remakeEngine = false;
                        if (ked.isIncPercent() && percent < 100) {
                            remakeEngine = true;
                            percent += 5;
                        }
                        if (ked.isDecPercent() && percent >= 20) {
                            remakeEngine = true;
                            percent -= 5;
                        }
                        TimeUnit.MILLISECONDS.sleep(10);
                        if (remakeEngine) {
                            makeScreenAndEngine();
                            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(ked);
                            ked = new KeyDispatcher(MainFrame.this, graphicsEngine, screen);
                            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ked);
                        }
                        frameNanoTime = System.nanoTime() - now;
                    } catch (InterruptedException | FontFormatException | IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        SoundSystem.init();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                graphicsThread.start();
                gameThread.start();
            }
        });
    }

    int percent = 100;

    private void makeScreenAndEngine() throws FontFormatException, IOException {
        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        screen = new ScreenBuffer(rect.width * percent / 100, rect.height * percent / 100);
        graphicsEngine = new GraphicsEngine(world, screen, semaphore);
    }

    private KeyDispatcher ked;

    private final Runnable repainter = new Runnable() {

        @Override
        public void run() {
            repaint();
        }
    };

    private boolean fullScreen = false;

    public void toggleFullScreen() {
        fullScreen = !fullScreen;
    }

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

}
