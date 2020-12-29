package meta;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import util.DateBean;
import util.TimeBean;
import static util.img.loadBufferedImage;

public class ApachePOI {

    static ArrayList<BufferedImage> img_from_docx(File file) {
        XWPFDocument docx;
        try {
            docx = new XWPFDocument(new FileInputStream(file));
        } catch (Exception | Error e) {
            return null;
        }
        ArrayList result = new ArrayList();
        for (XWPFPictureData pd : docx.getAllPictures()) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(pd.getData());
                result.add(ImageIO.read(bais));
                bais.close();
            } catch (Exception | Error ignored) {
            }
        }
        try {
            docx.close();
        } catch (Exception | Error e) {
            return null;
        }
        return result;
    }

    static ArrayList<BufferedImage> img_from_doc(File file) {
        HWPFDocument doc;
        try {
            doc = new HWPFDocument(new FileInputStream(file));
        } catch (Exception | Error e) {
            return null;
        }
        ArrayList result = new ArrayList();
        for (Picture pic : doc.getPicturesTable().getAllPictures()) {
            try {
                File tempimg = new File("data/tempimg");
                FileOutputStream outputStream = new FileOutputStream(tempimg);
                outputStream.write(pic.getContent());
                outputStream.close();
                BufferedImage bimg = loadBufferedImage(new File("data/tempimg"));
                tempimg.delete();
                if (bimg != null) {
                    result.add(bimg);
                }
            } catch (Exception | Error e) {
            }
        }
        return result;
    }

    public static String getDocText(File file) {
        WordExtractor extractor;
        try {
            //NPOIFS или POIFS??
            NPOIFSFileSystem fs = new NPOIFSFileSystem(file);
            extractor = new WordExtractor(fs.getRoot());
        } catch (Exception | Error e) {
            return null;
        }
        //static stripFields???
        StringBuilder s = new StringBuilder();
        for (String rawText : extractor.getParagraphText()) {
            s.append(WordExtractor.stripFields(rawText));
        }
        return s.toString();
    }

    public static boolean AddDocTags(File file, Map<String, String> map) {
        POIFSFileSystem fs;
        try {
            fs = new POIFSFileSystem(file);
        } catch (Exception | Error e) {
            return false;
        }
        DirectoryEntry root = fs.getRoot();
        DocumentInputStream stream;
        try {
            stream = fs.createDocumentInputStream(info);
        } catch (Exception | Error e) {
            return false;
        }
        byte[] content = new byte[stream.available()];
        try {
            stream.read(content);
        } catch (IOException ex) {
            return false;
        }
        stream.close();
        return rawDocSummaryInfo(content, map);
    }

    //строка "summaryinformation" с кодом 5 в начале строки
    private static final String info = new String(new byte[]{5, 83, 117, 109, 109, 97, 114, 121, 73, 110, 102, 111, 114, 109, 97, 116, 105, 111, 110});

    private static String fields[] = {
        "Неизвестное поле",//0x00
        "Кодировка полей метаданных",//0x01
        "Название",//0x02
        "Тема",//0x03
        "Автор",//0x04
        "Ключевые слова",//0x05
        "Комментарии",//0x06
        "Шаблон",//0x07
        "Последний автор",//0x08
        "Редакция",//0x09
        "Время редактирования",//0x0A
        "Дата последней печати",//0x0B
        "Дата создания",//0x0C
        "Дата последнего сохранения",//0x0D
        "Количество страниц",//0x0E
        "Количество слов",//0x0F
        "Количество символов",//0x10
        "Неизвестное поле",//11
        "Имя программы",//0x12
        "Защищенный"//0x13
    };

    public static boolean rawDocSummaryInfo(byte[] a, Map map) {
        //ТОЛЬКО КОДИРОВКА WINDOWS-1251 !!!
        ByteBuffer bb = ByteBuffer.wrap(a);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.position(44);
        int base = bb.getInt(), size = bb.getInt(), n = bb.getInt();
        System.out.println();
        for (; n > 0; n--) {
            short type = bb.getShort();
            bb.getShort();//есть padding из нулей
            int offset = bb.getInt();
            String value = getValue(bb, type, base + offset);
            if (type == 0x01 && value != null && !value.equals("1251")) {
                return false;
            }
            if (type < fields.length) {
                if (value != null) {
                    map.put(fields[type], value);
                } else {
                    System.err.println("RawDoc: null value for type " + type);
                }
            } else {
                System.err.println("RawDoc: uknown type " + type);
            }
        }
        return true;
    }

    private static String getValue(ByteBuffer bb, short type, int addr) {
        switch (type) {
            case 0x01:
                return String.valueOf(bb.getShort(addr + 4));
            case 0x02:
            case 0x03:
            case 0x04:
            case 0x05:
            case 0x06:
            case 0x07:
            case 0x08:
            case 0x09:
            case 0x12:
                return getString(bb, addr);
            case 0x0A:
                return TimeBean.valueOf(bb.getLong(addr + 4) / 10000000).toString();
            case 0x0B:
            case 0x0C:
            case 0x0D:
                return DateBean.MSFileTimeToDateBean(bb.getLong(addr + 4)).toString();
            case 0x0E:
            case 0x0F:
            case 0x10:
            case 0x13:
                return String.valueOf(bb.getInt(addr + 4));
            default:
                return null;
        }
    }

    private static String getString(ByteBuffer bb, int addr) {
        int n = 0, pos = bb.position();
        bb.position(addr + 8);//8 байт на инф. про строку
        while (true) {
            byte b = bb.get();
            if (b == 0) {
                break;
            }
            n++;
        }
        if (n == 0) {
            bb.position(pos);
            return "";
        }
        byte bytes[] = new byte[n];
        bb.position(bb.position() - n - 1);
        bb.get(bytes, 0, n);
        bb.position(pos);
        try {
            return new String(bytes, "windows-1251");
        } catch (Exception | Error e) {
            return null;
        }
    }
}
