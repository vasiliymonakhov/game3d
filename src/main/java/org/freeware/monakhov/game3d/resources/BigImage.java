package org.freeware.monakhov.game3d.resources;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.freeware.monakhov.game3d.ScreenBuffer;

/**
 * Комбинированное изображение. Если изображение содержит в себе слишком много прозрачных участков, то лучше
 * разбить его на несколько частей, которые можно будет потом отрисовать параллельно, а прозрачные участки не рисовать вообще
 * @author Vasily Monakhov
 */
public class BigImage {

    /**
     * Узел для элемента списка
     */
    private static class Node {
        /**
         * Изображение
         */
        private final BufferedImage i;
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
        Node(BufferedImage i, int x, int y) {
            this.i = i;
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Ширина полного изображения
     */
    private final static int WIDTH = 1920;
    /**
     * Высота полного изображения
     */
    private final static int HEIGHT = 1080;

    private final static int CHUNK_WIDTH = 60;
    private final static int CHUNK_HEIGHT = 60;

    /**
     * Создаёт комбинированное изображение
     */
    public BigImage(String fileName) throws IOException {
        BufferedImage bi = ImageIO.read(BigImage.class.getResourceAsStream(fileName));
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();
        for (int x = 0; x < WIDTH / CHUNK_WIDTH; x++) {
            for (int y = 0; y < HEIGHT / CHUNK_HEIGHT; y++) {
                int ix = x * CHUNK_WIDTH;
                int iy = y * CHUNK_HEIGHT;
                BufferedImage subImage = bi.getSubimage(ix, iy, CHUNK_WIDTH, CHUNK_HEIGHT);
                int opaq = checkChunkTransparency(subImage);
                if (opaq != 0) {
                    BufferedImage image = gfx_config.createCompatibleImage(CHUNK_WIDTH, CHUNK_HEIGHT, opaq == 1 ? Transparency.OPAQUE : Transparency.TRANSLUCENT);
                    image.setAccelerationPriority(1);
                    Graphics2D g = (Graphics2D) image.createGraphics();
                    g.drawImage(subImage, 0, 0, null);
                    g.dispose();
                    nodes.add(new Node(image, x, y));
                }
            }
        }
    }

    private static int  checkChunkTransparency(BufferedImage bi) {
        int transparent = 0;
        int opaque = 0;
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                int argb = bi.getRGB(x, y);
                int alpha = argb >>> 24;
                if (alpha == 0) {
                    transparent++;
                } else if (alpha == 255) {
                    opaque++;
                }
            }
        }
        int pixels = bi.getWidth() * bi.getHeight();
        if (transparent == pixels) return 0;
        if (opaque == pixels) return 1;
        return 2;
    }


    /**
     * Список частей изображения
     */
    private final List<Node> nodes = new ArrayList<>();

    /**
     * Карта для хранения изображений
     */
    private final static Map<String, BigImage> bigImages = new LinkedHashMap<>();

    /**
     * Добавляет комбинированное изображение
     * @param id идентификатор
     * @return ссылка на созданное комбинированное изображение
     * @throws IllegalArgumentException
     */
    public static BigImage add(String id, String fileName) throws IllegalArgumentException, IOException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("BigImage id is null or empty");
        }
        BigImage mi = new BigImage(fileName);
        bigImages.put(id, mi);
        return mi;
    }

    /**
     * Возвращает изображение по его идентификатору
     * @param id идентификатор
     * @return изображение
     */
    public static BigImage get(String id) {
        BigImage img = bigImages.get(id);
        if (img == null) {
            throw new IllegalArgumentException("BigImage " + id + " not exists");
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
            // вычислим координаты и размер в соответствии с масштабом
            x = n.x * CHUNK_WIDTH * screen.getWidth() / WIDTH;
            y = n.y * CHUNK_HEIGHT * screen.getHeight() / HEIGHT;
            int x1 = (n.x + 1) * CHUNK_WIDTH * screen.getWidth() / WIDTH;
            int y1 = (n.y  + 1) * CHUNK_HEIGHT * screen.getHeight() / HEIGHT;
            int w = x1 - x;
            int h = y1 - y;
            // создадим новое изображение, совместимое с буфером
            i = gfx_config.createCompatibleImage(w, h, n.i.getColorModel().getTransparency());
            i.setAccelerationPriority(1);
            Graphics2D g = (Graphics2D) i.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            // рисуем его красиво
            g.drawImage(n.i, 0, 0, w, h, null);
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
