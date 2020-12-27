package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import static meta.db.mimedb;
import static meta.type.parseFieldValue;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class xml {

    private static final Map<String, String> DocxNames = mimedb.get("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    public static boolean AddDocxTags(File file, Map<String, String> map) {
        ZipFile zip;
        try {
            zip = new ZipFile(file);
        } catch (IOException ex) {
            return false;
        }
        for (Enumeration e = zip.entries(); e.hasMoreElements();) {
            ZipEntry entry = (ZipEntry) e.nextElement();
            if (entry.isDirectory()) {
                continue;
            }
            switch (entry.getName()) {
                case "docProps/core.xml":
                case "docProps/app.xml":
                    break;
                default:
                    continue;
            }
            InputStream inputStream;
            try {
                inputStream = zip.getInputStream(entry);
                if (!load_xml(inputStream, map)) {
                    System.out.println("load_xml error");
                    return false;
                }
                inputStream.close();
            } catch (Exception | Error ee) {
                System.out.println("load_xml exception");
                ee.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private static boolean load_xml(InputStream inputStream, Map<String, String> map) {
        DocumentBuilder documentBuilder;
        Document document;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = documentBuilder.parse(inputStream);
        } catch (Exception | Error e) {
            return false;
        }
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String name = DocxNames.get(node.getNodeName());
            if (name == null) {
                System.err.println("DocxNames does not have name: " + node.getNodeName());
                return false;
            }
            Object value;
            if (name.equals("Время редактирования")) {
                value = TimeBean.valueOf(Long.valueOf(node.getTextContent()));
            } else {
                value = parseFieldValue(name, node.getTextContent());
            }
            if (value == null) {
                System.err.println("DocxNames null object: " + node.getNodeName() + " -> " + node.getTextContent());
                return false;
            }
            map.put(name, value.toString());
        }
        return true;
    }
}
