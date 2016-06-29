package org.freeware.monakhov.game3d.map;

import java.awt.image.BufferedImage;
import java.util.LinkedHashSet;
import java.util.Set;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Линия комнаты на карте
 *
 * @author Vasily Monakhov
 */
public class Line {

    /**
     * Начальная точка линии, должна находиться слева от линии взгляда
     * наблюдателя
     */
    protected final Point start;

    /**
     * Конечная точка линии, должна находиться слева от линии взгляда
     * наблюдателя
     */
    protected final Point end;
    
    /**
     * Эта линия может быть порталом в другую комнату
     */
    private Set<Room> portalToRooms;
    
    protected final World world;

    /**
     * Создаёт линию
     *
     * @param start начальная точка
     * @param end конечная точка
     * @param world
     */
    public Line(Point start, Point end, World world) {
        this.start = start;
        this.end = end;
        this.world = world;
    }

    /**
     * Возвращает начало линии
     *
     * @return начало линии
     */
    public Point getStart() {
        return start;
    }

    /**
     * Возвращает конец линии
     *
     * @return конец линии
     */
    public Point getEnd() {
        return end;
    }

    /**
     * Устанавливает комнату, превращая линию в портал
     *
     * @param portalTo в какую комнату ведёт портал
     */
    public void setPortal(Room portalTo) {
        if (portalToRooms == null) {
            portalToRooms = new LinkedHashSet<>();
        }
        portalToRooms.add(portalTo);
    }

    /**
     * Возвращает комнату, в которую ведёт этот портал
     *
     * @return комната, в которую ведёт этот портал
     */
    public Set<Room> getRoomsFromPortal() {
        return portalToRooms;
    }

    public boolean isPortal() {
        return portalToRooms!= null; 
    }
    
    /**
     * Сообщает, видима ли эта линия
     *
     * @return true - линия видима
     */
    public boolean isVisible() {
        return false;
    }
    
    public boolean pointIsVisible(Point p) {
        return false;
    }
    
    protected boolean everSeen = false;    
    
    public boolean isEverSeen() {
        return everSeen;
    }
    
    /**
     * Сообщает, что линию можно пересекать
     * @return можно ли пересекать через линию
     */
    public boolean isCrossable() {
        return true;
    }

    private final Point lcp = new Point();
    
    /**
     * Проверяет видимость линии на экране
     */
    boolean checkVisibility(Line[] mapLines, Point viewPoint, Point[] rayPoints, Point[] intersectPoints) {
        for (int i = 0; i < mapLines.length; i++) {
            if (SpecialMath.lineIntersection(start, end, rayPoints[i], viewPoint, lcp)) {
                if (lcp.between(start, end) && lcp.between(viewPoint, rayPoints[i])) return true;
            }
        }
        return false;
    }

    public BufferedImage getSubImage(Point p, int height) {
        return null;
    }
    
    public void onInteractWith(WorldObject wo) {
    }
    
    public void doSomething(long frameNanoTime) {
    }
    
}
