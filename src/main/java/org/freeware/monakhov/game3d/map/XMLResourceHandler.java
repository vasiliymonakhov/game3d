package org.freeware.monakhov.game3d.map;

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
    /**
     * Текущее комбинированное изображение
     */
    private MultiImage currentMultiImage;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attr)  throws SAXException {
        try {
            switch(qName) {
                case "textures" :
                    path = attr.getValue("path");
                    break;
                case "sprites" :
                    path = attr.getValue("path");
                    break;
                case "images" :
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
                    if (currentMultiImage != null) {
                        currentMultiImage.addImage(Image.get(attr.getValue("id")), Integer.parseInt(attr.getValue("x")), Integer.parseInt(attr.getValue("y")));
                    } else {
                        Image.add(attr.getValue("id"), path + attr.getValue("file"));
                    }
                    break;
                case "multiimage" :
                    currentMultiImage = MultiImage.add(attr.getValue("id"), Integer.parseInt(attr.getValue("width")), Integer.parseInt(attr.getValue("height")));
                    break;
            }
        }
        catch (IOException | NumberFormatException ex) {
            Logger.getLogger(XMLResourceHandler.class.getName()).log(Level.SEVERE, "Error loading resource", ex);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)  throws SAXException {
            switch(qName) {
                case "multiimage" :
                    currentMultiImage = null;
                    break;
            }
    }

}
