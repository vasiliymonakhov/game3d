package org.freeware.monakhov.game3d.map;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.freeware.monakhov.game3d.ScreenBuffer;

/**
 * Комбинированное изображение. Если изображение содержит в себе слишком много прозрачных участков, то лучше
 * разбить его на несколько частей, которые можно будет потом отрисовать параллельно, а прозрачные участки не рисовать вообще
 * @author Vasily Monakhov
 */
public class MultiImage {

    /**
     * Узел для элемента списка
     */
    private static class Node {
        /**
         * Изображение
         */
        private final Image i;
        /**
         * Горизонтальная координата оригинального изображения
         */
        private final int x;
        /**
         * Вертикальная координата оригинального изображения
         */
        private final int y;

        /**
         * Создаёт узел
         * @param i изображение
         * @param x горизонталная координата
         * @param y вертикальная координата
         */
        Node(Image i, int x, int y) {
            this.i = i;
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Ширина полного изображения
     */
    private final int width;
    /**
     * Высота полного изображения
     */
    private final int height;

    /**
     * Создайт комбинированное изображение
     * @param width ширина
     * @param height высота
     */
    public MultiImage(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Список частей изображения
     */
    private final List<Node> nodes = new ArrayList<>();

    /**
     * Добавляет новое иображение в комбинированное изображение
     * @param i изображене участка
     * @param x горизонталная координата участка в изображении
     * @param y вертикальная координата участка в изображении
     */
    void addImage(Image i, int x, int y) {
        if (i == null) {
            throw new IllegalArgumentException("Image is null or empty");
        }
        nodes.add(new Node(i, x, y));
    }

    /**
     * Карта для хранения изображений
     */
    private final static Map<String, MultiImage> multiImages = new LinkedHashMap<>();

    /**
     * Добавляет комбинированное изображение
     * @param id идентификатор
     * @param width ширина
     * @param height высоте
     * @return ссылка на созданное комбинированное изображение
     * @throws IllegalArgumentException
     */
    public static MultiImage add(String id, int width, int height) throws IllegalArgumentException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("MultiImage id is null or empty");
        }
        MultiImage mi = new MultiImage(width, height);
        multiImages.put(id, mi);
        return mi;
    }

    /**
     * Возвращает изображение по его идентификатору
     * @param id идентификатор
     * @return изображение
     */
    public static MultiImage get(String id) {
        MultiImage img = multiImages.get(id);
        if (img == null) {
            throw new IllegalArgumentException("MultiImage " + id + " not exists");
        }
        return img;
    }

    /**
     * Участок комбинированного изображения, пригодный для отображения в буфере экрана
     */
    public class ImageToDraw {

        /**
         * Буферизированное изображение, преобразованное в нужный размер
         */
        private final BufferedImage i;
        /**
         * Координаты изображения, преобразованные в соответсвии с масштабом
         */
        private final int x;
        private final int y;

        /**
         * Создаёт участок
         * @param n узел списка
         * @param screen буфер экрана
         */
        private ImageToDraw (Node n, ScreenBuffer screen) {
            GraphicsConfiguration gfx_config = GraphicsEnvironment.
                    getLocalGraphicsEnvironment().getDefaultScreenDevice().
                    getDefaultConfiguration();
            // вычислим координаты и размер в соответсвии с масштабом
            x = (int)Math.round((double)n.x * screen.getWidth() / width);
            y = (int)Math.round((double)n.y * screen.getHeight() / height);
            int w = (int)Math.round((double)n.i.getImage().getWidth() * screen.getWidth() / width);
            int h = (int)Math.round((double)n.i.getImage().getHeight() * screen.getHeight() / height);
            // создадим новое изображение, совместимое с буфером
            i = gfx_config.createCompatibleImage(w, h, n.i.getImage().getColorModel().getTransparency());
            i.setAccelerationPriority(1);
            Graphics2D g = (Graphics2D) i.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            // рисуем его красиво
            g.drawImage(n.i.getImage(), 0, 0, w, h, null);
            g.dispose();
        }

        /**
         * Возвращает буферизированное изображение
         * @return the i
         */
        public BufferedImage getI() {
            return i;
        }

        /**
         * Возвращает горизонтальную координату участка изображения
         * @return the x
         */
        public int getX() {
            return x;
        }

        /**
         * Возвращает вертикальную координату участка изображения
         * @return the y
         */
        public int getY() {
            return y;
        }

    }

    /**
     * Список подготовленных изображений
     */
    private List<ImageToDraw> imagesToDraw;
    /**
     * Размеры буфера изображений, для которого были подготовлены изображения
     */
    private int sw = 0, sh = 0;

    /**
     * Возвращает список изображений, пригодных для отрисовки в буфере экрана
     * @param screen буфер экрана
     * @return список изображений
     */
    public List<ImageToDraw> getImagesToDraw(ScreenBuffer screen) {
        if (imagesToDraw == null) {
            // ранее список не запрашивался - создаём новый
            imagesToDraw = new ArrayList<>();
        }
        // если размеры буфера экрана изменились, нужно перезаполнить список
        if (sw != screen.getWidth() || sh != screen.getHeight()) {
            imagesToDraw.clear();
            sw = screen.getWidth();
            sh = screen.getHeight();
            for (Node n : nodes) {
                imagesToDraw.add(new ImageToDraw(n, screen));
            }
        }
        return imagesToDraw;
    }

}
