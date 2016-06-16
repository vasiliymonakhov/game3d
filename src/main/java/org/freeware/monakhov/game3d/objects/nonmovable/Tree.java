package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;

/**
 * Дерево
 * @author Vasily Monakhov 
 */
public class Tree extends NonMovableObject {

    public Tree(Point position) {
        super(position);
    }

    @Override
    public Sprite getSprite() {
        return Sprite.get("tree");
    }

    @Override
    public double getRadius() {
        return 60;
    }

}
