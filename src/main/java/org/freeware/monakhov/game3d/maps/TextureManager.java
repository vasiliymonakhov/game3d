package org.freeware.monakhov.game3d.maps;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Менеджер текстур
 * @author Vasily Monakhov 
 */
public class TextureManager {
    
    /**
     * Карта для хранения текстур
     */
    private final Map<String, Texture> textures = new LinkedHashMap<>();
    
    /**
     * Добавляет новую текстуру
     * @param id идентификатор текстуры
     * @param fileName имя файла с текстурой
     * @throws IOException 
     */
    public void add(String id, String fileName) throws IOException {
        if (textures.containsKey(id)) {
            throw new IllegalArgumentException("Texture " + id + " already exists");             
        }
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Texture id is null or empty");             
        }        
        textures.put(id, new Texture(fileName));
    }
    
    public Texture get(String id) {
        Texture tex = textures.get(id);
        if (tex== null) {
            throw new IllegalArgumentException("Texture " + id + " not exists"); 
        }
        return tex;
    }

}
