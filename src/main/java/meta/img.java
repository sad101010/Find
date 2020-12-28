package meta;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Tag;
import java.io.File;
import java.util.Map;
import static meta.ApachePOI.img_from_doc;
import static meta.ApachePOI.img_from_docx;
import static meta.PDFBox.img_from_pdf;
import static meta.db.mimedb;
import static meta.type.getFieldType;
import util.DateBean;
import static util.util.get_mime;

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
