package meta;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import static util.util.getPath;

public class Tika {

    static void add_text_from_file(Map map, File file) {
        if (file == null || file.length() == 0) {
            return;
        }
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        try {
            parser.parse(new FileInputStream(file), handler, new Metadata(), new ParseContext());
            map.put("Внутренний текст", handler.toString());
        } catch (Exception | Error e) {
        }
    }

    public static Metadata dirt(File file) {
        if (file == null || file.length() == 0) {
            return null;
        }
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        try {
            parser.parse(new FileInputStream(file), new BodyContentHandler(), metadata, new ParseContext());
            /*System.out.println(getPath(file));
            for(String name:metadata.names()){
                if(metadata.isMultiValued(name)){
                    continue;
                }
                System.out.println("dirt: "+name+" --> "+metadata.get(name));
            }*/
            return metadata;
        } catch (Exception | Error e) {
            return null;
        }
    }

    public static String get_mime(File file) {
        if (file == null || file.length() == 0) {
            return null;
        }
        org.apache.tika.Tika tika = new org.apache.tika.Tika();
        try {
            return tika.detect(file);
        } catch (Exception | Error e) {
            return null;
        }
    }
}
