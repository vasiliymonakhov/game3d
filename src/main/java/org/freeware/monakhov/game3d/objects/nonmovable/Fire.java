package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;

/**
 * Огонь
 * @author Vasily Monakhov 
 */
public class Fire extends CrossableObject {

    private final Sprite[] sprites = new Sprite[16];
    int index;
    
    public Fire(Point position) {
        super(position);
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = Sprite.get(String.format("fire%02d", i));
        }
    }

    @Override
    public Sprite getSprite() {
        index = (index + 1) % sprites.length;
        return sprites[index];
    }
    
    @Override
    public boolean isCrossable() {
        return true;
    }    

    @Override
    public double getRadius() {
        return 75;
    }

}
