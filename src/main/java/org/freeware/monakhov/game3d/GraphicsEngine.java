package org.freeware.monakhov.game3d;

import org.freeware.monakhov.game3d.objects.WorldObject;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.freeware.monakhov.game3d.map.visiblelines.Door;
import org.freeware.monakhov.game3d.map.Image;
import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.MultiImage;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.visiblelines.VisibleLine;
import org.freeware.monakhov.game3d.map.World;

/**
 * Графическая система
 *
 * @author Vasily Monakhov
 */
public class GraphicsEngine {

    /**
     * Текущий мир
     */
    private final World world;

    /**
     * Экран, на котором всё будет рисоваться
     */
    private final ScreenBuffer screen;

    /**
     * Дальняя граница мира. Дельше этой величины рисовать смысла нет
     */
    private final double farFarFrontier;

    /**
     * Массив, в котором определяется, какая линия видна в данном столюце экрана
     */
    private final VisibleLine[] mapLines;
    /**
     * Точки на дальней границе мира, соответсвующие столбцам экрана. Через этуи
     * точки и главного героя проходят лучи, трассирующие всё пространство мира,
     */
    private final Point[] rayPoints;
    /**
     * Трансформированные точки, развёрнутые относительно текущей позиции
     * главного героя
     */
    private final Point[] transformedRayPoints;
    /**
     * Точки пересечения трассирующих лучей и линий на карте мира
     */
    private final Point[] wallsIntersectPoints;
    /**
     * Предварительно расчитанные коэффициенты, корректирующие перспективные
     * искажения
     */
    private final double[] k;

    /**
     * Объекты для многопоточного рисования пола и потолка
     */
    private final FloorCeilingDrawer[] floorCeilingDrawers;
    /**
     * Объекты дял многопоточного рисования участков неба
     */
    private final SkyDrawer[] skyDrawers;

    /**
     * Высота стены в пикселях мира
     */
    public final static double WALL_SIZE = 256;
    /**
     * Корректирующий коэффициент
     */
    public final static double KORRECTION = 10000;

    /**
     * Количество одновременно работающих потоков рисования. Разумно указать
     * здесь количество ядер, следует учитывать, что бездумное увеличение этого
     * параметра может снизить скорость работы.
     */
    public static final int MAX_DRAW_THREADS = 4;

    /**
     * Пул потоков для многопоточного рисования
     */
    private final static Executor EXECUTOR = Executors.newFixedThreadPool(MAX_DRAW_THREADS);
    /**
     * Отсортированный список объектов мира, которые необходимо нарисовать
     */
    private final ArrayList<WorldObject> objectsSortList = new ArrayList<>();

    /**
     * Изображение неба
     */
    private BufferedImage sky;

    /**
     * Создаёт графическую систему
     *
     * @param world мир
     * @param screen экран
     */
    GraphicsEngine(World world, ScreenBuffer screen) {
        this.world = world;
        this.screen = screen;
        mapLines = new VisibleLine[screen.getWidth()];
        farFarFrontier = KORRECTION * screen.getWidth() / 2;
        rayPoints = new Point[screen.getWidth()];
        transformedRayPoints = new Point[screen.getWidth()];
        int ix = screen.getWidth() / 2 - 1;
        for (int i = 0; i < rayPoints.length; i++) {
            rayPoints[i] = new Point((i - ix) * KORRECTION, farFarFrontier);
            transformedRayPoints[i] = new Point();
        }
        k = new double[screen.getWidth()];
        for (int i = 0; i < screen.getWidth(); i++) {
            k[i] = WALL_SIZE * SpecialMath.lineLength(world.getHero().getPosition(), rayPoints[i]) / KORRECTION;
        }
        wallsIntersectPoints = new Point[screen.getWidth()];
        for (int i = 0; i < screen.getWidth(); i++) {
            wallsIntersectPoints[i] = new Point();
        }
        floorCeilingDrawers = new FloorCeilingDrawer[MAX_DRAW_THREADS];
        int w = screen.getWidth() / floorCeilingDrawers.length;
        BufferedImage floor = world.getFloor() != null ? Image.get(world.getFloor()).getImage() : null;
        BufferedImage ceiling = world.getCeiling() != null ? Image.get(world.getCeiling()).getImage() : null;
        for (int i = 0; i < floorCeilingDrawers.length; i++) {
            floorCeilingDrawers[i] = new FloorCeilingDrawer(i * w, w, floor, ceiling);
        }
        if (world.getSky() != null) {
            // если предусмотрено небо, то создаём его изображение в разрешении экрана
            BufferedImage bi = Image.get(world.getSky()).getImage();
            GraphicsConfiguration gfx_config = GraphicsEnvironment.
                    getLocalGraphicsEnvironment().getDefaultScreenDevice().
                    getDefaultConfiguration();
            int width = bi.getWidth();
            int height = bi.getHeight();
            sky = gfx_config.createCompatibleImage(screen.getWidth() * 4, screen.getHeight() / 2, Transparency.OPAQUE);
            sky.setAccelerationPriority(1);
            Graphics2D g2 = (Graphics2D) sky.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(bi, 0, 0, sky.getWidth(), sky.getHeight(), 0, 0, width, height, null);
            g2.dispose();
            skyDrawers = new SkyDrawer[MAX_DRAW_THREADS];
            w = screen.getWidth() / skyDrawers.length;
            for (int i = 0; i < skyDrawers.length; i++) {
                skyDrawers[i] = new SkyDrawer(i * w, w, sky);
            }
        } else {
            skyDrawers = null;
        }
    }

    /**
     * Подготавливает к рендерингу
     */
    void clearRender() {
        Arrays.fill(mapLines, null);
        visibleRooms.clear();
    }

    /**
     * Список видимых комнат мира
     */
    private final Set<Room> visibleRooms = new HashSet<>();
    /**
     * Список проверенных по ходу дела порталов
     */
    private final Set<Room> checkedRooms = new HashSet<>();

    /**
     * Проверяет, какие комнаты видимы. При этом будет заполнен массив столбцов
     * с указанием конкретных линий
     */
    void checkVisibleRooms() {
        for (int i = 0; i < screen.getWidth(); i++) {
             checkedRooms.clear();
             world.getHero().getRoom().traceRoom(mapLines, i, world.getHero().getPosition(), transformedRayPoints[i], wallsIntersectPoints[i], visibleRooms, checkedRooms);
        }
    }

    /**
     * Класс для отображения видимого столбца
     */
    class VisibleWallColumn implements Runnable {

        /**
         * изображение столбца из текстуры
         */
        private final BufferedImage bi;
        /**
         * Горизонталная координата на экране
         */
        private final int x;
        /**
         * Высота столбца
         */
        private final int h;
        /**
         * Ширина столбца
         */
        private int w = 1;

        /**
         * Создаёт столбец
         *
         * @param bi столбец текстуры
         * @param x горизонтальная координата на экране
         * @param h высота столбца
         */
        VisibleWallColumn(BufferedImage bi, int x, int h) {
            this.bi = bi;
            this.x = x;
            this.h = h;
        }

        /**
         * Проверяет, являются ли переданные пересетры такими же
         *
         * @param bi столбец текстуры
         * @param h выота столбца
         * @return true если то же самое
         */
        boolean sameAs(BufferedImage bi, int h) {
            return this.bi == bi && this.h == h;
        }

        /**
         * Увеличивает ширину на 1
         */
        void incWidth() {
            w++;
        }

        /**
         * Рисует столбец на заданном графическом контексте
         *
         */
        @Override
        public void run() {
            try {
                int ch = (int) ((screen.getHeight() - h) / 2);
                g.drawImage(bi, x, ch, w, h, null);
            } finally {
                doneSignal.countDown();
            }
        }

        /**
         * графический контекст
         */
        private Graphics2D g;
        /**
         * сигнал завершения работы
         */
        private CountDownLatch doneSignal;

        /**
         * Подготавливает к рисованию
         *
         * @param g графический контекст
         * @param doneSignal сигнал завершения работы
         */
        void prepare(Graphics2D g, CountDownLatch doneSignal) {
            this.g = g;
            this.doneSignal = doneSignal;
        }

    }

    /**
     * видимые столбцы стен
     */
    private final List<VisibleWallColumn> visibleWallColumns = new ArrayList<>();

    /**
     * Подготавливает список видимых столбцов стен
     */
    private void prepareVisibleWallColumns() {
        visibleWallColumns.clear();
        VisibleWallColumn lastColumn = null;
        int h = 0;
        BufferedImage bi = null;
        for (int i = 0; i < mapLines.length; i++) {
            VisibleLine l = mapLines[i];
            // определяем линию
            if (l != null) {
                // вычисляем расстояние от главного героя до точки пересечения трассирующего луча и линии
                double dist = SpecialMath.lineLength(world.getHero().getPosition(), wallsIntersectPoints[i]);
                // вычисляем видимую высоту столбца
                h = (int)Math.round(k[i] / dist);
                // получаем столбец текстуры
                bi = l.getSubImage(wallsIntersectPoints[i], h);
            }
            if (lastColumn == null) {
                lastColumn = new VisibleWallColumn(bi, i, h);
                visibleWallColumns.add(lastColumn);
            } else {
                if (lastColumn.sameAs(bi, h)) {
                    lastColumn.incWidth();
                } else {
                    lastColumn = new VisibleWallColumn(bi, i, h);
                    visibleWallColumns.add(lastColumn);
                }
            }
        }
    }

    /**
     * Рисует стены
     *
     * @param g графический контекст
     * @throws InterruptedException
     */
    void renderWalls(Graphics2D g) throws InterruptedException {
        prepareVisibleWallColumns();
        CountDownLatch doneSignal = new CountDownLatch(visibleWallColumns.size());
        // для всех столбцов экрана запускаем потоки рисования
        for (VisibleWallColumn vwc : visibleWallColumns) {
            vwc.prepare(g, doneSignal);
            EXECUTOR.execute(vwc);
        }
        // ожидаем завершения всех потоков
        doneSignal.await();
    }

    /**
     * Класс для многопоточного рисования пола и потолка
     */
    private class FloorCeilingDrawer implements Runnable {

        /**
         * горизонталная координата участка, за которую будет отвечать экземпляр
         * класса
         */
        private final int x;
        /**
         * Объект блокировки - сигнал завершения работы
         */
        private CountDownLatch doneSignal;
        /**
         * Графически контекст
         */
        private Graphics2D g;
        /**
         * Изображение для пола
         */
        private final BufferedImage floor;
        /**
         * Изображение для потолка
         */
        private final BufferedImage ceiling;
        /**
         * высота столбца пола
         */
        private final int floorHeight;

        /**
         * Подготавливает объект к работе
         *
         * @param g графический контекст
         * @param doneSignal объект блокировки - сигнал завершения работы
         */
        void prepare(Graphics2D g, CountDownLatch doneSignal) {
            this.g = g;
            this.doneSignal = doneSignal;
        }

        /**
         * Создаёт объект
         *
         * @param x горизонтальная координата участка пола
         * @param w ширина участка
         * @param floor изображение для пола
         * @param ceiling изображение для потолка
         */
        FloorCeilingDrawer(int x, int w, BufferedImage floor, BufferedImage ceiling) {
            this.x = x;
            GraphicsConfiguration gfx_config = GraphicsEnvironment.
                    getLocalGraphicsEnvironment().getDefaultScreenDevice().
                    getDefaultConfiguration();
            int height = screen.getHeight() / 2;
            floorHeight = screen.getHeight() - height;
            // содаём заранее подогнанные под нужный размер изображения
            this.floor = gfx_config.createCompatibleImage(w, floorHeight, Transparency.OPAQUE);
            Graphics2D g2 = (Graphics2D) this.floor.getGraphics();
            g2.drawImage(floor, 0, 0, w, floorHeight, null);
            // учитываем, что потолка может и не быть
            if (ceiling != null) {
                this.ceiling = gfx_config.createCompatibleImage(w, height, Transparency.OPAQUE);
                g2 = (Graphics2D) this.ceiling.getGraphics();
                g2.drawImage(ceiling, 0, 0, w, height, null);
            } else {
                this.ceiling = null;
            }
        }

        @Override
        public void run() {
            try {
                // если потолок есть, то рисуем его
                if (ceiling != null) {
                    g.drawImage(ceiling, x, 0, null);
                }
                // рисуем пол
                g.drawImage(floor, x, screen.getHeight() - floorHeight, null);
            } finally {
                // сообщаем о завершении работы
                doneSignal.countDown();
            }
        }

    }

    /**
     * Рисует пол и потолок
     *
     * @param g графический контекст
     * @throws InterruptedException
     */
    void renderCeilingAndFloor(Graphics2D g) throws InterruptedException {
        CountDownLatch doneSignal = new CountDownLatch(floorCeilingDrawers.length);
        // рисуем пол и потолок в нескольких потоках
        for (FloorCeilingDrawer floorCeilingDrawer : floorCeilingDrawers) {
            floorCeilingDrawer.prepare(g, doneSignal);
            EXECUTOR.execute(floorCeilingDrawer);
        }
        doneSignal.await();
        // если надо, рисуем небо
        if (skyDrawers != null) {
            doneSignal = new CountDownLatch(floorCeilingDrawers.length);
            double pi2 = Math.PI * 2;
            // определяем смещение на панорамном изображении неба в зависимости от азимута главного героя
            int a = (int) (sky.getWidth() * ((world.getHero().getAzimuth() + pi2) % pi2) / pi2);
            for (SkyDrawer skyDrawer : skyDrawers) {
                skyDrawer.prepare(g, a, doneSignal);
                EXECUTOR.execute(skyDrawer);
            }
            doneSignal.await();
        }

    }

    /**
     * Класс для многопоточного рисования неба
     */
    private class SkyDrawer implements Runnable {

        /**
         * Горизонталная координата участка неба
         */
        private final int x;
        /**
         * Ширина участка неба
         */
        private final int w;
        /**
         * Смещение участка от края изображения
         */
        private int a;
        /**
         * Сигнал завершения работы
         */
        private CountDownLatch doneSignal;
        /**
         * Графический контекст
         */
        private Graphics2D g;
        /**
         * Панорамное изображение неба
         */
        private final BufferedImage sky;

        /**
         * Подготавливает работу
         *
         * @param g графический контекст
         * @param a смещение
         * @param doneSignal сигнал заершения работы
         */
        void prepare(Graphics2D g, int a, CountDownLatch doneSignal) {
            this.g = g;
            this.doneSignal = doneSignal;
            this.a = a;
        }

        /**
         * Создаёт объект
         *
         * @param x горизонтальная координата участка неба
         * @param w ширина участка
         * @param sky панорамное изображение неба
         */
        SkyDrawer(int x, int w, BufferedImage sky) {
            this.x = x;
            this.w = w;
            this.sky = sky;
        }

        @Override
        public void run() {
            try {
                //
                int xx = (a + x) % sky.getWidth();
                if (xx + w <= sky.getWidth()) {
                    //если участок помещается на панорамное изображение, то рисуем его целиком
                    g.drawImage(sky.getSubimage(xx, 0, w, sky.getHeight()), x, 0, null);
                } else {
                    // участок не помещается, его надо разделить на две части
                    int w1 = sky.getWidth() - xx;
                    g.drawImage(sky.getSubimage(xx, 0, w1, sky.getHeight()), x, 0, null);
                    int w2 = w - w1;
                    int xx2 = (xx + w1) % sky.getWidth();
                    g.drawImage(sky.getSubimage(xx2, 0, w2, sky.getHeight()), x + w1, 0, null);
                }
            } finally {
                // посылаем сигнал завершения
                doneSignal.countDown();
            }
        }
    }

    /**
     * Компаратор для сортировки объектов мира. Сначала должны идти те объекты,
     * которые находятся дальше от главного героя
     */
    Comparator<WorldObject> objectsSortComparator = new Comparator<WorldObject>() {
        @Override
        public int compare(WorldObject o1, WorldObject o2) {
            return (int) (o2.distanceTo(world.getHero().getPosition()) - o1.distanceTo(world.getHero().getPosition()));
        }
    };

    /**
     * Спрайт, который необходимо нарисовать на экране
     */
    private class SpriteToDraw implements Runnable {

        /**
         * Спрайт
         */
        private final Sprite sprite;
        /**
         * Горизонтальное смещение начала спрайта
         */
        private final int spriteXStartOffset;
        /**
         * Ширина изображения спрайта
         */
        private final int spriteImgWidth;
        /**
         * высота столбца для спрайта
         */
        private final int h;
        /**
         * координаты и размеры видимого участка спрайта
         */
        private final int x, y, width, height;

        /**
         * Создаёт объект
         *
         * @param sprite спрайт
         * @param spriteXStartOffset горизонтальное смещение начала спрайта
         * @param spriteImgWidth ширина изображения спрайта
         * @param h высота столвца мира, в ктороый попал спрайт
         * @param x горизонтальная координата видимого участка спрайта
         * @param y вертикальная координата видимого участка спрайта
         * @param width ширина видимого участка спрайта
         * @param height вісота видимого участка спрайта
         */
        SpriteToDraw(Sprite sprite, int spriteXStartOffset, int spriteImgWidth, int h, int x, int y, int width, int height) {
            this.sprite = sprite;
            this.spriteXStartOffset = spriteXStartOffset;
            this.spriteImgWidth = spriteImgWidth;
            this.h = h;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        /**
         * Определяет, пересекаются ли два спрайта на экране.
         *
         * @param s1 первый спрайт
         * @param s2 второй спрайт
         * @return true если пересекаются
         */
        boolean intersects(SpriteToDraw s1, SpriteToDraw s2) {
            if (s1.x > s2.x) {
                // поменять местами спрайты для сравнения
                SpriteToDraw stmp = s1;
                s1 = s2;
                s2 = stmp;
            }
            if (s1.x + s1.width > s2.x) {
                // пересекаются по горизонтали
                if (s1.y > s2.y) {
                    return s2.y + s2.height > s1.y;
                } else {
                    return s1.y + s1.height > s2.y;
                }
            }
            return false;
        }

        /**
         * Проверяет, пересекается ли этот спрайт с другим. Если спрайты не
         * пересекаются, то их можно нарисовать одновременно в разных потоках
         *
         * @param os другой спрайт
         * @return true если пересекаются
         */
        boolean intersects(SpriteToDraw os) {
            return intersects(this, os);
        }

        /**
         * Рисует спрайт
         *
         * @param g графический контекст
         */
        void drawSprite(Graphics2D g) {
            g.drawImage(sprite.getSubImage(spriteXStartOffset, 0, spriteImgWidth, h),
                    x, y, width, height, null);
        }

        @Override
        public void run() {
            try {
                // рисуем спрайт :)
                drawSprite(g);
            } finally {
                // сообщаем о завершении работы
                doneSignal.countDown();
            }
        }

        /**
         * сигнал завершения работы
         */
        private CountDownLatch doneSignal;
        /**
         * графический контекст
         */
        private Graphics2D g;

        /**
         * подготоваливает к работе
         * @param doneSignal сигнал окончания работы
         */
        void prepare(Graphics2D g, CountDownLatch doneSignal) {
            this.g = g;
            this.doneSignal = doneSignal;
        }

    }

    /**
     * Список спрайтов, которых нужно нарисовать
     */
    private final List<SpriteToDraw> spritesToDraw = new ArrayList<>();

    /**
     * Подготавливает объект к отрисовке
     *
     * @param wo объект
     */
    void prepareToRenderObject(WorldObject wo) {
        Point rsp = new Point();
        int spriteXStartOffset = 0;
        int spriteXEndOffset = -0;
        int spriteXStart = 0;
        int spriteXEnd = 0;
        int h = 0;
        int sh = 0;
        int syo = 0;
        int ch = 0;
        boolean started = false;
        for (int i = 0; i < screen.getWidth(); i++) {
            // протрассируем объект, определим, попадает ли он в поле зрения
            if (SpecialMath.lineIntersection(wo.getLeft(), wo.getRight(), world.getHero().getPosition(), wallsIntersectPoints[i], rsp)) {
                if (rsp.between(wo.getLeft(), wo.getRight()) && rsp.between(world.getHero().getPosition(), wallsIntersectPoints[i])) {
                    // объект явно попадает в поле зрения и находится перед, а не за стеной
                    if (!started) {
                        // запомним необходимые для отрисовки данные из крайней левой точки спрайта
                        double dist = SpecialMath.lineLength(world.getHero().getPosition(), rsp);
                        double kk = SpecialMath.lineLength(world.getHero().getPosition(), transformedRayPoints[i]);
                        h = (int) Math.round(GraphicsEngine.WALL_SIZE * kk / (dist * GraphicsEngine.KORRECTION));
                        sh = (int) Math.round(h * wo.getSprite().getHeight() / GraphicsEngine.WALL_SIZE);
                        syo = (int) Math.round(h * wo.getSprite().getYOffset() / GraphicsEngine.WALL_SIZE);
                        ch = (screen.getHeight() - h) / 2;
                        spriteXStartOffset = (int) SpecialMath.lineLength(wo.getLeft(), rsp);
                        spriteXStart = i;
                        started = true;
                    }
                    if (started) {
                        // потенциально каждый столбец спрайта также и последний
                        spriteXEndOffset = (int)  SpecialMath.lineLength(wo.getLeft(), rsp);
                        spriteXEnd = i;
                    }
                }
            }
        }
        if (started) {
            // спрайт виден, определяем дополнительные параметры
            int spriteImgWidth = spriteXEndOffset - spriteXStartOffset;
            if (spriteImgWidth > wo.getSprite().getWidth()) {
                spriteImgWidth = wo.getSprite().getWidth();
            } else if (spriteImgWidth < 1) {
                spriteImgWidth = 1;
            }
            int visibleSpriteWidth = spriteXEnd - spriteXStart;
            if (visibleSpriteWidth < 1) {
                visibleSpriteWidth = 1;
            }
            // запоминаем спрайт в списке спрайтов, подлежащих рисованию
            spritesToDraw.add(new SpriteToDraw(wo.getSprite(), spriteXStartOffset, spriteImgWidth, h, spriteXStart, ch + syo, visibleSpriteWidth, sh));
        }
    }

    /**
     * Рисует на экране видимые объекты
     *
     * @param g графический контекст
     * @throws InterruptedException
     */
    void renderObjects(Graphics2D g) throws InterruptedException {
        objectsSortList.clear();
        for (WorldObject wobj : world.getAllObjects()) {
            wobj.turnSpriteToViewPoint(world.getHero()); // повернуть спрайт к главному герою
            boolean flag = false;
            // проверить по списку видимых комнат
            for (Room r : visibleRooms) {
                if (r.insideThisRoom(wobj.getLeft()) || r.insideThisRoom(wobj.getRight())) {
                    // если спрайт хотя бы частично пересекает одну из видимых комнат, его надо проверить на видимость
                    flag = true;
                    break;
                }
            }
            if (flag || visibleRooms.contains(wobj.getRoom())) {
                objectsSortList.add(wobj);
            }
        }
        // отсортировать объекты по расстоянию
        Collections.sort(objectsSortList, objectsSortComparator);
        spritesToDraw.clear();
        // определить видимые спрайты
        for (WorldObject wobj : objectsSortList) {
            prepareToRenderObject(wobj);
        }
        // если видимых спрайтов нет, то на этом можно заканчивать
        if (spritesToDraw.isEmpty()) {
            return;
        }
        // по всей видимости, видимые спрайты есть, надо их рисовать
        List<SpriteToDraw> currentDrawSprites = new ArrayList<>();
        Iterator<SpriteToDraw> d2dit = spritesToDraw.iterator();
        // первый спрайт попадает в очередь рисования автоматически
        currentDrawSprites.add(d2dit.next());
        // перебираем спрайты, пока они не закончатся
        while (d2dit.hasNext()) {
            SpriteToDraw s2d = d2dit.next();
            boolean canAdd;
            if (currentDrawSprites.size() < MAX_DRAW_THREADS) {
                canAdd = true;
                // проверяем, есть ли в списке непересекающиеся спрайты, их нужно нарисовать параллельно
                for (SpriteToDraw sd : currentDrawSprites) {
                    if (sd.intersects(s2d)) {
                        canAdd = false;
                        break;
                    }
                }
            } else {
                canAdd = false;
            }
            if (canAdd) {
                // да, новый спрайт не пересекается ни с одним из уже добавленных в список, будем рисовать их параллельно
                currentDrawSprites.add(s2d);
            } else {
                // нет, новый спрайт пересекает кого-то в списке, надо сначала нарисовать спрайты в списке и начать с начала
                drawSpritesInParallel(g, currentDrawSprites);
                // кандидат станет первым в списке
                currentDrawSprites.add(s2d);
            }
        }
        // если спрайты закочились, но не были нарисованы, нарисовать их
        if (!currentDrawSprites.isEmpty()) {
            drawSpritesInParallel(g, currentDrawSprites);
        }
    }

    /**
     * Рисует спрайты в нескольких потоках
     *
     * @param drawers список объектов для рисования
     * @throws InterruptedException
     */
    private void drawSpritesInParallel(Graphics2D g, List<SpriteToDraw> drawers) throws InterruptedException {
        CountDownLatch doneSignal = new CountDownLatch(drawers.size());
        // для каждого объекта запускаем свой поток
        for (SpriteToDraw sd : drawers) {
            sd.prepare(g, doneSignal);
            EXECUTOR.execute(sd);
        }
        // ожидаем завершения работы
        doneSignal.await();
        // очищаем список
        drawers.clear();
    }

    /**
     * Класс для рисования комбинированных изображений
     */
    private class MultiImageDrawer implements Runnable {

        /**
         * Участок комбинированного изображения
         */
        private final MultiImage.ImageToDraw mi2d;
        /**
         * Графический контекст
         */
        private final Graphics2D g;
        /**
         * сигнал завершения
         */
        private final CountDownLatch doneSignal;

        /**
         * Создайт объект
         *
         * @param g графический контекст
         * @param doneSignal сигнал завершения
         * @param mi2d участок комбинированного изображения
         */
        MultiImageDrawer(Graphics2D g, CountDownLatch doneSignal, MultiImage.ImageToDraw mi2d) {
            this.g = g;
            this.doneSignal = doneSignal;
            this.mi2d = mi2d;
        }

        @Override
        public void run() {
            try {
                g.drawImage(mi2d.getI(), mi2d.getX(), mi2d.getY(), null);
            } finally {
                doneSignal.countDown();
            }
        }
    }

    /**
     * Рисует то, как выглядит герой в своих глазах - руки, оружие и т.п.
     *
     * @param g графический контекст
     * @throws InterruptedException
     */
    private void renderHeroLook(Graphics2D g) throws InterruptedException {
        List<MultiImage.ImageToDraw> images = world.getHero().getImagesToDraw(screen);
        CountDownLatch doneSignal = new CountDownLatch(images.size());
        for (final MultiImage.ImageToDraw mi2d : images) {
            EXECUTOR.execute(new MultiImageDrawer(g, doneSignal, mi2d));
        }
        doneSignal.await();
    }

    /**
     * Признак, рисовать ли карту
     */
    private boolean mapEnabled;

    /**
     * Переключает отображение карты
     */
    void toggleMap() {
        mapEnabled = !mapEnabled;
    }

    /**
     * Рисует всё что нужно
     *
     * @throws InterruptedException
     */
    void render() throws InterruptedException {
        Graphics2D g = (Graphics2D) screen.getImage().createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        renderCeilingAndFloor(g);
        renderWalls(g);
        renderObjects(g);
        renderHeroLook(g);
        if (mapEnabled) {
            drawMap(g);
        }
        drawFPS(g);
        g.dispose();
    }

    double counter;
    long time = System.nanoTime();
    private final Font f = new Font(Font.SANS_SERIF, 0, 25);
    String fps = "";

    void drawFPS(Graphics2D g) {
        counter++;
        if (counter >= 50) {
            long nt = System.nanoTime();
            fps = String.format("FPS = %d", (int) (1e9 * counter / (nt - time)));
            time = nt;
            counter = 0;
        }
        g.setColor(Color.GREEN);
        g.setFont(f);
        g.drawString(fps, 25, 25);
    }

    /**
     * Вращает сканирующие лучи вокруг главного героя
     */
    void transformRays() {
        double sin = Math.sin(-world.getHero().getAzimuth());
        double cos = Math.cos(-world.getHero().getAzimuth());
        for (int i = 0; i < screen.getWidth(); i++) {
            double x = rayPoints[i].getX();
            double y = rayPoints[i].getY();
            double new_x = x * cos - y * sin + world.getHero().getPosition().getX();
            double new_y = y * cos + x * sin + world.getHero().getPosition().getY();
            transformedRayPoints[i].moveTo(new_x, new_y);
        }
    }

    /**
     * Выполняет рабочий цикл по отображению одного кадра
     *
     * @throws InterruptedException
     */
    void doCycle() throws InterruptedException {
        clearRender();
        transformRays();
        checkVisibleRooms();
        render();
        screen.swapBuffers();
    }

    // толщины линий карты
    BasicStroke LINE = new BasicStroke(1);
    BasicStroke WALL = new BasicStroke(3);
    BasicStroke ROOM = new BasicStroke(5);

    /**
     * Масштаб карты
     */
    private double mapScale = 0.25;

    /**
     * Увеличивает масштаб вдвое
     */
    void incMapScale() {
        mapScale *= 2;
    }

    /**
     * Уменьшает масштаб
     */
    void decMapScale() {
        mapScale /= 2;
    }

    // цвета для участков карты
    private final Color WALL_COLOR = new Color(0, 255, 0, 128);
    private final Color UNSEEN_WALL_COLOR = new Color(128, 128, 128, 128);
    private final Color VIEWPOINT_COLOR = new Color(255, 0, 0, 128);

    /**
     * Рисует карту
     *
     * @param g графический контекст
     */
    void drawMap(Graphics2D g) {
        int dx = screen.getImage().getWidth() / 2;
        int dy = screen.getImage().getHeight() / 2;
        g.setColor(VIEWPOINT_COLOR);
        g.fillOval(dx - 5, dy - 5, 10, 10);
        g.setStroke(WALL);
        int rx = (int) (20 * Math.sin(world.getHero().getAzimuth()));
        int ry = (int) (20 * Math.cos(world.getHero().getAzimuth()));
        g.drawLine(dx, dy, dx + rx, dy - ry);
        dx += -(int) world.getHero().getPosition().getX() * mapScale;
        dy += (int) world.getHero().getPosition().getY() * mapScale;
        for (Room r : world.getAllRooms()) {
            for (Line l : r.getAllLines()) {
                if (!l.isEverSeen()) {
                    g.setColor(UNSEEN_WALL_COLOR);
                } else {
                    g.setColor(WALL_COLOR);
                }
                if (r == world.getHero().getRoom()) {
                    g.setStroke(ROOM);
                } else if (l instanceof Door) {
                    g.setStroke(LINE);
                } else {
                    g.setStroke(WALL);
                }
                g.drawLine(dx + (int) (l.getStart().getX() * mapScale), dy - (int) (l.getStart().getY() * mapScale),
                        dx + (int) (l.getEnd().getX() * mapScale), dy - (int) (l.getEnd().getY() * mapScale));
            }
        }

    }

}
