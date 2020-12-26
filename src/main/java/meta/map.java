package meta;

import java.util.Map;
import java.util.TreeMap;

public class map {

    public static final Map<String,String> mime_map = new TreeMap<>();

    static {
        mime_map.put("Не задан", "Не задан");
        mime_map.put("application/msword", "doc,dot");
        mime_map.put("application/pdf", "pdf");
        mime_map.put("application/vnd.ms-excel", "xls,xlm,xla,xlc,xlt,xlw,xll,xld");
        mime_map.put("application/vnd.oasis.opendocument.spreadsheet", "ods");
        mime_map.put("application/vnd.oasis.opendocument.text", "odt");
        mime_map.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
        mime_map.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
        mime_map.put("image/jpeg", "jpg,jpeg,jpe,jif,jfif,jfi");
        mime_map.put("image/png", "png");
        mime_map.put("application/rtf", "rtf");
    }
}
