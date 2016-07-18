package org.freeware.monakhov.game3d.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * загружает ресурсы из XML
 * 
 * @author Vasily Monakhov 
 */
public class XMLResourceLoader {

    private final SAXParser saxParser;    
   
    public XMLResourceLoader() throws ParserConfigurationException, SAXException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        saxParser = spf.newSAXParser();
    }

    public void parse(InputStream is) throws UnsupportedEncodingException, SAXException, IOException {
        try (InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader bisr = new BufferedReader(isr)) {
            saxParser.parse(new InputSource(bisr), new XMLResourceHandler());
        }
    }
    
}
