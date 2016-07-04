package org.freeware.monakhov.game3d.objects.movable;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import org.freeware.monakhov.game3d.Screen;
import org.freeware.monakhov.game3d.map.Image;
import org.freeware.monakhov.game3d.map.MultiImage;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;

/**
 *
 * @author Vasily Monakhov 
 */
public class Hero extends ViewPoint {

    public Hero(World world, Point position) {
        super(world, position);
    }
    
    public static final double MAX_BACKWARD_MOVE_SPEED = -256 / 1.0E9;
    public static final double STRAFE_ACCELERATION = 512 / 1.0E18;
    public static final double TURN_ACCELERATION = 2 * Math.PI / 1.0E18;
    public static final double MOVE_BREAKING = 4096 / 1.0E18;
    public static final double MOVE_FORWARD_ACCELERATION = 2048 / 1.0E18;
    public static final double MAX_STRAFE_SPEED = 1024 / 1.0E9;
    public static final double STRAFE_BREAKING = 4096 / 1.0E18;
    public static final double MOVE_BACKWARD_ACCELERATION = -512 / 1.0E18;
    public static final double MAX_FORWARD_MOVE_SPEED = 1024 / 1.0E9;
    public static final double TURN_BREAK = 3 * Math.PI / 1.0E18;
    public static final double MAX_TURN_SPEED = Math.PI / 2.0E9;
    
    private double strafeSpeed = 0;
    private double moveSpeed = 0;
    private double turnSpeed = 0;
    
    private void analyseTurn(boolean left, boolean right, long frameNanoTime) {
        if (left) {
            if (turnSpeed > 0) {
                turnSpeed = turnSpeed - TURN_BREAK * frameNanoTime;
            } else {
                turnSpeed = turnSpeed - TURN_ACCELERATION * frameNanoTime;
            }
            if (turnSpeed < -MAX_TURN_SPEED) {
                turnSpeed = -MAX_TURN_SPEED;
            }
        } else if (right) {
            if (turnSpeed < 0) {
                turnSpeed = turnSpeed + TURN_BREAK * frameNanoTime;
            } else {
                turnSpeed = turnSpeed + TURN_ACCELERATION * frameNanoTime;
            }
            if (turnSpeed > MAX_TURN_SPEED) {
                turnSpeed = MAX_TURN_SPEED;
            }
        } else {
            if (turnSpeed > 0) {
                turnSpeed = turnSpeed - TURN_BREAK * frameNanoTime;
                if (turnSpeed < 0) {
                    turnSpeed = 0;
                }
            } else if (turnSpeed < 0) {
                turnSpeed = turnSpeed + TURN_BREAK * frameNanoTime;
                if (turnSpeed > 0) {
                    turnSpeed = 0;
                }
            }
        }        
    }
    
    private void analyseMove(boolean forward, boolean backward, long frameNanoTime) {
        if (forward) {
            if (moveSpeed < 0) {
                moveSpeed = moveSpeed + MOVE_BREAKING * frameNanoTime;
            } else {
                moveSpeed = moveSpeed + MOVE_FORWARD_ACCELERATION * frameNanoTime;
            }
            if (moveSpeed > MAX_FORWARD_MOVE_SPEED) {
                moveSpeed = MAX_FORWARD_MOVE_SPEED;
            }
        } else if (backward) {
            if (moveSpeed > 0) {
                moveSpeed = moveSpeed - MOVE_BREAKING * frameNanoTime;
            } else {
                moveSpeed = moveSpeed + MOVE_BACKWARD_ACCELERATION * frameNanoTime;
            }
            if (moveSpeed < MAX_BACKWARD_MOVE_SPEED) {
                moveSpeed = MAX_BACKWARD_MOVE_SPEED;
            }
        } else {
            if (moveSpeed > 0) {
                moveSpeed = moveSpeed - MOVE_BREAKING * frameNanoTime;
                if (moveSpeed < 0) {
                    moveSpeed = 0;
                }
            } else if (moveSpeed < 0) {
                moveSpeed = moveSpeed + MOVE_BREAKING * frameNanoTime;
                if (moveSpeed > 0) {
                    moveSpeed = 0;
                }
            }
        }        
    }
    
    private void analyseStrafe(boolean strafeLeft, boolean strafeRight, long frameNanoTime) {
        if (strafeLeft) {
            if (strafeSpeed > 0) {
                strafeSpeed = strafeSpeed - STRAFE_BREAKING * frameNanoTime;
            } else {
                strafeSpeed = strafeSpeed - STRAFE_ACCELERATION * frameNanoTime;
            }
            if (strafeSpeed < -MAX_STRAFE_SPEED) {
                strafeSpeed = -MAX_STRAFE_SPEED;
            }
        } else if (strafeRight) {
            if (strafeSpeed < 0) {
                strafeSpeed = strafeSpeed + STRAFE_BREAKING * frameNanoTime;
            } else {
                strafeSpeed = strafeSpeed + STRAFE_ACCELERATION * frameNanoTime;
            }
            if (strafeSpeed > MAX_STRAFE_SPEED) {
                strafeSpeed = MAX_STRAFE_SPEED;
            }
        } else {
            if (strafeSpeed > 0) {
                strafeSpeed = strafeSpeed - STRAFE_BREAKING * frameNanoTime;
                if (strafeSpeed < 0) {
                    strafeSpeed = 0;
                }
            } else if (strafeSpeed < 0) {
                strafeSpeed = strafeSpeed + STRAFE_BREAKING * frameNanoTime;
                if (strafeSpeed > 0) {
                    strafeSpeed = 0;
                }
            }
        }        
    }
    
    public void analyseKeys(boolean left, boolean right, boolean forward, boolean backward, boolean strafeLeft, boolean strafeRight, long frameNanoTime) {
        analyseTurn(left, right, frameNanoTime);
        setAzimuth(getAzimuth() + turnSpeed * frameNanoTime);
        analyseMove(forward, backward, frameNanoTime);
        analyseStrafe(strafeLeft, strafeRight, frameNanoTime);
        moveBy(moveSpeed * frameNanoTime, strafeSpeed * frameNanoTime);
    }
    
    MultiImage weapon = MultiImage.get("axe");
    
    public List<MultiImage.ImageToDraw> getImagesToDraw(Screen screen) {
        return weapon.getImagesToDraw(screen);
    }

}
