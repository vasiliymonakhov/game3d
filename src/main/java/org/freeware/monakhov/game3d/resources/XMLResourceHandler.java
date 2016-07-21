package org.freeware.monakhov.game3d.resources;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * обрабатывает XML с ресурсами
 * @author Vasily Monakhov
 */
class XMLResourceHandler extends DefaultHandler {

    /**
     * Путь к файлам
     */
    private String path;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attr)  throws SAXException {
        try {
            switch(qName) {
                case "textures" :
                case "sprites" :
                case "images" :
                case "audiofiles" :
                    path = attr.getValue("path");
                    break;
                case "texture" :
                    Texture.add(attr.getValue("id"), path + attr.getValue("file"));
                    break;
                case "sprite" :
                    Sprite.add(attr.getValue("id"),
                            Integer.parseInt(attr.getValue("width")),
                            Integer.parseInt(attr.getValue("height")),
                            Integer.parseInt(attr.getValue("y_offset")),
                            path + attr.getValue("file"));
                    break;
                case "image" :
                    Image.add(attr.getValue("id"), path + attr.getValue("file"));
                    break;
                case "bigimage" :
                    BigImage.add(attr.getValue("id"), path + attr.getValue("file"));
                    break;
                case "audiofile" :
                    AudioFile.add(attr.getValue("id"), path + attr.getValue("file"), Integer.parseInt(attr.getValue("priority")));
            }
        }
        catch (IOException | NumberFormatException ex) {
            Logger.getLogger(XMLResourceHandler.class.getName()).log(Level.SEVERE, "Error loading resource", ex);
        }
    }


}
