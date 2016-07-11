package org.freeware.monakhov.game3d.map.visiblelines;

import java.awt.image.BufferedImage;
import java.util.Set;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.Texture;
import org.freeware.monakhov.game3d.map.World;

/**
 * Видимая линия
 *
 * @author Vasily Monakhov
 */
public abstract class VisibleLine extends Line {

    /**
     * Создаёт линию
     *
     * @param start точка начала
     * @param end точка конца
     * @param world мир
     */
    public VisibleLine(Point start, Point end, World world) {
        super(start, end, world);
    }

    /**
     * Вовзращает текстуру
     *
     * @return
     */
    public abstract Texture getTexture();

    /**
     * Трассируем линию лучом
     * @param mapLines массив линий, попадающих в столбец на экране
     * @param index индекс луча
     * @param viewPoint точка обзора
     * @param rayPoint точка луча
     * @param intersectPoint точека пересечения трассирующего луча и какой-либо стены
     * @param visibleRooms множество видимых комнат
     * @param checkedRooms множество уже проверенных комнат
     * @return true если эта стена видима или она есть дверь и за ней в комнате нашлась видимая стена
     */
    @Override
    public boolean traceLine(VisibleLine[] mapLines, int index, Point viewPoint, Point rayPoint, Point intersectPoint, Set<Room> visibleRooms, Set<Room> checkedRooms) {
        Point lcp = new Point();
        if (SpecialMath.lineIntersection(start, end, rayPoint, viewPoint, lcp) && lcp.between(start, end) && lcp.between(viewPoint, rayPoint)) {
            if (pointIsVisible(lcp)) {
                mapLines[index] = this;
                intersectPoint.moveTo(lcp.getX(), lcp.getY());
                everSeen = true;
                return true;
            } else {
                if (isPortal()) {
                    for (Room r : getRoomsFromPortal()) {
                        if (checkedRooms.contains(r)) {
                            continue;
                        }
                        if (r.traceRoom(mapLines, index, viewPoint, rayPoint, intersectPoint, visibleRooms, checkedRooms)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Вовзращает участок изображения линии
     *
     * @param p точка на линии
     * @param height высота столбца
     * @return участок изображения
     */
    public abstract BufferedImage getSubImage(Point p, double height);

}
