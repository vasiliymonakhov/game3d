/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handles XML data of our resources
 * @author Vasily Monakhov 
 */
class XMLResourceHandler extends DefaultHandler {
    
    private String path;
    
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
                    Sprite.add(attr.getValue("id"), path + attr.getValue("file"), Integer.parseInt(attr.getValue("y_offset")));
                    break;                
                case "image" :
                    Image.add(attr.getValue("id"), path + attr.getValue("file"));
                    break;                
            }            
        }
        catch (IOException | NumberFormatException ex) {
            Logger.getLogger(XMLResourceHandler.class.getName()).log(Level.SEVERE, "Error loading resource", ex);
        }
    }        

}
