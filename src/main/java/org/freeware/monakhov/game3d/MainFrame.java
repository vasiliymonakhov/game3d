package org.freeware.monakhov.game3d;

import java.awt.Color;
import org.freeware.monakhov.game3d.objects.movable.Hero;
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

    GraphicsEngine engine;
    World world;
    Screen screen;
    Hero hero;

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
        XMLWorldLoader loader = new XMLWorldLoader();
        loader.parse(world, hero, MainFrame.class.getResourceAsStream("/org/freeware/monakhov/game3d/map/testWorld1.xml"));

        screen = new Screen(rect.width * 3 / 4, rect.height * 3 / 4);
        engine = new GraphicsEngine(world, hero, screen);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyDispatcher());
        Timer sec = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fps = String.format("%d FPS%n", frames);
                frames = 0;
            }
        });
        sec.start();
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        long now = System.nanoTime();
                        analyseKeys();
                        try {
                            engine.doCycle();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        frames++;
                        frameNanoTime = System.nanoTime() - now;
                        SwingUtilities.invokeLater(repainter);
                    } catch (Throwable th) {
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
    public void paint(Graphics g) {
        Rectangle rr = this.getBounds();
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

    boolean left;
    boolean right;
    boolean forward;
    boolean backward;
    boolean strafeLeft;
    boolean strafeRight;

    final static double MAX_TURN_SPEED = Math.PI / 2.0e9;
    final static double TURN_ACCELERATION = 2 * Math.PI / 1.0e18;
    final static double TURN_BREAK = 3 * Math.PI / 1.0e18;
    final static double STRAFE_SPEED = 1024 / 1.0e9;
    final static double MAX_FORWARD_MOVE_SPEED = 1024 / 1.0e9;
    final static double MAX_BACKWARD_MOVE_SPEED = -256 / 1.0e9;
    final static double MOVE_FORWARD_ACCELERATION = 2048 / 1.0e18;
    final static double MOVE_BACKWARD_ACCELERATION = -512 / 1.0e18;
    final static double MOVE_BREAKING = 4096 / 1.0e18;

    double moveSpeed = 0;
    double turnSpeed = 0;

    void analyseKeys() {
        if (left) {
            if (turnSpeed > 0) {
                turnSpeed -= TURN_BREAK * frameNanoTime;
            } else {
                turnSpeed -= TURN_ACCELERATION * frameNanoTime;
            }
            if (turnSpeed < -MAX_TURN_SPEED) {
                turnSpeed = -MAX_TURN_SPEED;
            }
        } else if (right) {
            if (turnSpeed < 0) {
                turnSpeed += TURN_BREAK * frameNanoTime;
            } else {
                turnSpeed += TURN_ACCELERATION * frameNanoTime;
            }
            if (turnSpeed > MAX_TURN_SPEED) {
                turnSpeed = MAX_TURN_SPEED;
            }
        } else {
            if (turnSpeed > 0) {
                turnSpeed -= TURN_BREAK * frameNanoTime;
                if (turnSpeed < 0) {
                    turnSpeed = 0;
                }
            } else if (turnSpeed < 0) {
                turnSpeed += TURN_BREAK * frameNanoTime;
                if (turnSpeed > 0) {
                    turnSpeed = 0;
                }
            }
        }
        double ts = turnSpeed * frameNanoTime;
        hero.setAzimuth(hero.getAzimuth() + ts);
        if (forward) {
            if (moveSpeed < 0) {
                moveSpeed += MOVE_BREAKING * frameNanoTime;
            } else {
                moveSpeed += MOVE_FORWARD_ACCELERATION * frameNanoTime;
            }
            if (moveSpeed > MAX_FORWARD_MOVE_SPEED) {
                moveSpeed = MAX_FORWARD_MOVE_SPEED;
            }
        } else if (backward) {
            if (moveSpeed > 0) {
                moveSpeed -= MOVE_BREAKING * frameNanoTime;
            } else {
                moveSpeed += MOVE_BACKWARD_ACCELERATION * frameNanoTime;
            }
            if (moveSpeed < MAX_BACKWARD_MOVE_SPEED) {
                moveSpeed = MAX_BACKWARD_MOVE_SPEED;
            }
        } else {
            if (moveSpeed > 0) {
                moveSpeed -= MOVE_BREAKING * frameNanoTime;
                if (moveSpeed < 0) {
                    moveSpeed = 0;
                }
            } else if (moveSpeed < 0) {
                moveSpeed += MOVE_BREAKING * frameNanoTime;
                if (moveSpeed > 0) {
                    moveSpeed = 0;
                }
            }
        }
        double ms = moveSpeed * frameNanoTime;
        double ss = 0;
        if (strafeRight) {
            ss = STRAFE_SPEED * frameNanoTime;
        } else if (strafeLeft) {
            ss = -STRAFE_SPEED * frameNanoTime;
        }
        hero.moveBy(ms, ss);
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
                        strafeLeft = true;
                        break;
                    case KeyEvent.VK_X:
                        strafeRight = true;
                        break;
                    case KeyEvent.VK_TAB:
                        engine.toggleMap();
                        break;
                    case KeyEvent.VK_F12:
                        fullScreen = !fullScreen;
                        break;
                    case KeyEvent.VK_PAGE_UP:
                        engine.incMapScale();
                        break;
                    case KeyEvent.VK_PAGE_DOWN:
                        engine.decMapScale();
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
                        strafeLeft = false;
                        break;
                    case KeyEvent.VK_X:
                        strafeRight = false;
                        break;
                }
            }
            return false;
        }
    }
}
