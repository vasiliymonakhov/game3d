package org.freeware.monakhov.game3d;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

/**
 *
 * @author Vasily Monakhov
 */
public class KeyDispatcher implements KeyEventDispatcher {

    private final MainFrame mainFrame;
    private final GraphicsEngine graphicsEngine;
    private final ScreenBuffer screen;

    public KeyDispatcher (MainFrame mainFrame, GraphicsEngine graphicsEngine, ScreenBuffer screen) {
        this.mainFrame = mainFrame;
        this.graphicsEngine = graphicsEngine;
        this.screen = screen;
    }

        private boolean strafeLeft;
        private boolean right;
        private boolean forward;
        private boolean backward;
        private boolean strafeRight;
        private boolean left;
        private boolean interact;
        private boolean incPercent;
        private boolean decPercent;
        private boolean firePressed;

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
                        mainFrame.toggleFullScreen();
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
                    case KeyEvent.VK_CONTROL:
                        firePressed = true;
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
                    case KeyEvent.VK_CONTROL:
                        firePressed = false;
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

        /**
         * @return the firePressed
         */
        public boolean isFirePressed() {
            return firePressed;
        }

        /**
         * @param firePressed the firePressed to set
         */
        public void setFirePressed(boolean firePressed) {
            this.firePressed = firePressed;
        }
    }