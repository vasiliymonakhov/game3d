package org.freeware.monakhov.game3d;

import java.awt.EventQueue;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * Главный класс, запускающий основное окно
 * @author Vasily Monakhov
 */
public class Game3D {

    /**
     * Запуск основного окна
     * @param args аргументы в командной строке
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
//                    System.setProperty("sun.java2d.opengl", "True");
                    System.setProperty("sun.java2d.accthreshold ", "0");
                    System.setProperty("sun.java2d.d3d", "True");
                    System.setProperty("sun.java2d.ddforcevram", "True");
                    System.setProperty("sun.java2d.transaccel", "True");
                    MainFrame mf = new MainFrame();
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(mf);
                    mf.setVisible(true);
                } catch (ParserConfigurationException | SAXException | IOException | FontFormatException ex) {
                    Logger.getLogger(Game3D.class.getName()).log(Level.SEVERE, "Не удалось запустить приложение", ex);
                }
            }
        });
    }

}
