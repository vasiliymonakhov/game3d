package org.freeware.monakhov.game3d;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author vasya
 */
public class Game3D {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainFrame().setVisible(true);
                } catch (ParserConfigurationException | SAXException | IOException ex) {
                    Logger.getLogger(Game3D.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
}
