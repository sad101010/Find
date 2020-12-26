package meta;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Tag;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static meta.Tika.get_mime;
import static meta.db.mimedb;
import static meta.type.getFieldType;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Picture;
import util.DateBean;
import static util.img.loadBufferedImage;

public class img {

    public static void add_images_from_document(Map map, File file) {
        String mime = get_mime(file);
        switch (mime) {
            case "application/msword":
                map.put("Изображения", img_from_doc(file));
                break;
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                map.put("Изображения", img_from_docx(file));
                break;
            case "application/pdf":
                map.put("Изображения", img_from_pdf(file));
                break;
        }
    }

    public static ArrayList<BufferedImage> img_from_pdf(File file) {
        PDDocument document;
        try {
            document = PDDocument.load(file);
        } catch (Exception | Error e) {
            System.err.println("err img_from_pdf");
            return null;
        }
        ArrayList res = new ArrayList();
        for (PDPage page : document.getPages()) {
            PDResources resources = page.getResources();
            for (COSName name : resources.getXObjectNames()) {
                PDXObject object;
                try {
                    object = resources.getXObject(name);
                } catch (Exception | Error e) {
                    continue;
                }
                if (object instanceof PDImageXObject) {
                    PDImageXObject image = (PDImageXObject) object;
                    try {
                        res.add(image.getImage());
                    } catch (Exception | Error e) {
                    }
                }
            }
        }
        try {
            document.close();
        } catch (Exception | Error e) {
            System.err.println("Ошибка закрытия pdf-документа");
        }
        return res;
    }

    public static ArrayList<BufferedImage> img_from_docx(File file) {
        XWPFDocument docx;
        try {
            docx = new XWPFDocument(new FileInputStream(file));
        } catch (Exception | Error e) {
            System.err.println("err img_from_docx: " + file.getPath());
            return null;
        }
        Iterator<XWPFPictureData> it = docx.getAllPictures().iterator();
        ArrayList result = new ArrayList();
        while (it.hasNext()) {
            byte[] data = it.next().getData();
            ByteArrayInputStream stream = new ByteArrayInputStream(data);
            try {
                result.add(ImageIO.read(stream));
            } catch (Exception | Error ignored) {
            }
        }
        try {
            docx.close();
        } catch (Exception | Error e) {
            System.err.println("Ошибка закрытия docx-документа");
        }
        return result;
    }

    public static ArrayList<BufferedImage> img_from_doc(File file) {
        HWPFDocument doc;
        try {
            doc = new HWPFDocument(new FileInputStream(file));
        } catch (Exception | Error e) {
            System.err.println("err img_from_doc: " + file.getPath());
            return null;
        }
        List<Picture> pics = doc.getPicturesTable().getAllPictures();
        ArrayList result = new ArrayList();
        for (int i = 0; i < pics.size(); i++) {
            Picture pic = (Picture) pics.get(i);
            try {
                FileOutputStream outputStream = new FileOutputStream(new File("data/tempimg"));
                outputStream.write(pic.getContent());
                outputStream.close();
                result.add(loadBufferedImage(new File("data/tempimg")));
            } catch (Exception | Error e) {
            }
        }
        return result;
    }

    static void addImgNames(Map map, File file) {
        String mime = get_mime(file);
        switch (mime) {
            case "image/jpeg":
            case "image/png":
                break;
            default:
                return;
        }
        Map<String, String> names = mimedb.get(mime);
        com.drew.metadata.Metadata metadata;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
        } catch (Exception | Error e) {
            return;
        }
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                int tagType = tag.getTagType();
                String DirAndTag=directory.getName()+"::"+tag.getTagName();
                /*
                System.out.println("DirAndTag> " + DirAndTag);
                System.out.println("Description> " + tag.getDescription());
                System.out.println("as boolean: " + directory.getBooleanObject(tagType));
                System.out.println("as string: " + directory.getString(tagType));
                System.out.println("as date: " + DateToString(directory.getDate(tagType)));
                System.out.println("as float: " + directory.getFloatObject(tagType));
                System.out.println("as double: " + directory.getDoubleObject(tagType));
                System.out.println("as int: " + directory.getInteger(tagType));
                System.out.println("as long: " + directory.getLongObject(tagType));
                System.out.println("-------------------------------------------");
                */
                String name = names.get(DirAndTag);
                if (name == null || name.equals(tag.getTagName())) {
                    continue;
                }
                if (map.containsKey(name)) {
                    System.err.println("metadata duplicate > " + tag.getTagName()+" == "+name);
                    continue;
                }
                Object value;
                switch (getFieldType(name)) {
                    case "@Long":
                        value = String.valueOf(directory.getLongObject(tagType));
                        break;
                    case "@DateBean":
                        value = DateBean.valueOf(directory.getDate(tagType)).toString();
                        break;
                    case "@String":
                        value = directory.getString(tagType);
                        break;
                    default:
                        continue;
                }
                if (value != null) {
                    map.put(name, value);
                }
            }
        }
    }
}
