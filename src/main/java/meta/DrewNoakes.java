package meta;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Tag;
import java.io.File;
import java.util.Map;
import static meta.db.mimedb;
import static meta.type.getFieldType;
import util.DateBean;
import static util.util.get_mime;

public class DrewNoakes {

    static void addImgTags(Map map, File file) {
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
                String DirAndTag = directory.getName() + "::" + tag.getTagName();
                String name = names.get(DirAndTag);
                if (name == null || map.containsKey(name)) {
                    continue;
                }
                Object value;
                switch (getFieldType(name)) {
                    case "@Long":
                        value = directory.getLongObject(tagType);
                        break;
                    case "@DateBean":
                        value = DateBean.valueOf(directory.getDate(tagType));
                        break;
                    case "@String":
                        //не проверено
                        value = directory.getObject(tagType);
                        break;
                    default:
                        continue;
                }
                if (value != null) {
                    map.put(name, value.toString());
                }
            }
        }
    }
}
