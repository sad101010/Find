package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import static util.img.argb_img_resize;

public class thumbsdb {

    private final ArrayList<String> pathList = new ArrayList();
    private final Map<String, Integer> pathMap = new TreeMap();

    synchronized public void add(String pathToFile) {
        File file = new File(pathToFile);
        BufferedImage thumb = mkthumb(file);
        if (thumb == null) {
            return;
        }
        if (pathMap.containsKey(pathToFile)) {
            return;
        }
        Integer id = pathMap.size() + 1;
        pathMap.put(pathToFile, id);
        pathList.add(pathToFile);
        try {
            ImageIO.write(thumb, "png", new File("data/thumbnails/" + id + ".png"));
        } catch (Exception | Error e) {
        }
    }

    public String getPathById(Integer id) {
        return pathList.get(id - 1);
    }

    public Integer getIdByPath(String path) {
        return pathMap.get(path);
    }

    private BufferedImage mkthumb(File file) {
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (Exception | Error e) {
            return null;
        }
        if (image == null) {
            //странно, должно было отловиться в try выше
            return null;
        }
        double a = 200.0 / Math.max(image.getHeight(), image.getWidth());
        int w = (int) (image.getWidth() * a);
        int h = (int) (image.getHeight() * a);
        if (w > image.getWidth() || h > image.getHeight()) {
            w = image.getWidth();
            h = image.getHeight();
        }
        return argb_img_resize(image, w, h);
    }
}
