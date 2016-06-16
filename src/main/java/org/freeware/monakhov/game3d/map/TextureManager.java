package org.freeware.monakhov.game3d.map;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Менеджер текстур
 * @author Vasily Monakhov 
 */
public class TextureManager {
    
    /**
     * Карта для хранения текстур
     */
    private final static Map<String, Texture> textures = new LinkedHashMap<>();
    
    /**
     * Добавляет новую текстуру
     * @param id идентификатор текстуры
     * @param fileName имя файла с текстурой
     * @throws IOException 
     */
    public static void add(String id, String fileName) throws IOException {
        if (textures.containsKey(id)) {
            throw new IllegalArgumentException("Texture " + id + " already exists");             
        }
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Texture id is null or empty");             
        }        
        textures.put(id, new Texture(fileName));
    }
    
    public static Texture get(String id) {
        Texture tex = textures.get(id);
        if (tex== null) {
            throw new IllegalArgumentException("Texture " + id + " not exists"); 
        }
        return tex;
    }

    static {
        try {
            add("brick01", "/org/freeware/monakhov/game3d/map/brick01.jpg");
            add("brick02", "/org/freeware/monakhov/game3d/map/brick02.jpg");    
            add("brick03", "/org/freeware/monakhov/game3d/map/brick03.jpg");
        } catch (IOException ex) {
            Logger.getLogger(TextureManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
