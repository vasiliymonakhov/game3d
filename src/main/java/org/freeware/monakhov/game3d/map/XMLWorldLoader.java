/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d.map;

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
 * Loads the world from XML stream
 *
 * @author Vasily Monakhov
 */
public class XMLWorldLoader {

    private final SAXParser saxParser;

    public XMLWorldLoader() throws ParserConfigurationException, SAXException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        saxParser = spf.newSAXParser();
    }

    public void parse(World world, InputStream is) throws UnsupportedEncodingException, SAXException, IOException {
        try (InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader bisr = new BufferedReader(isr)) {
            saxParser.parse(new InputSource(bisr), new XMLWorldHandler(world));
        }
    }

}
