package org.freeware.monakhov.game3d.objects.nonmovable;

import java.util.Random;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;

/**
 * Огонь
 * @author Vasily Monakhov 
 */
public class Fire extends CrossableObject {

    private final Sprite[] sprites = new Sprite[16];
    int index;
    
    public Fire(World world, Point position) {
        super(world, position);
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = Sprite.get(String.format("fire%02d", i));
        }
        Random r = new Random();
        index = r.nextInt(sprites.length);
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
