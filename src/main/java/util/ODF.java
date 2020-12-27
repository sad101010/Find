package util;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import static meta.db.mimedb;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ODF {

    private static final Map<String, String> OdtNames = mimedb.get("application/vnd.oasis.opendocument.text");

    public static boolean AddOdtTags(File file, Map<String, String> map) {
        //dc:date - неизвестный таг
        ZipFile zip;
        try {
            zip = new ZipFile(file);
        } catch (Exception | Error e) {
            return false;
        }
        ZipEntry entry = zip.getEntry("meta.xml");
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
        xml(document.getDocumentElement(), map);
        return true;
    }

    private static boolean isLeaf(Node node) {
        if (!node.hasChildNodes()) {
            return true;
        }
        NodeList nodeList = node.getChildNodes();
        if (nodeList.getLength() > 1) {
            return false;
        }
        if (nodeList.getLength() < 1) {
            return true;
        }
        return !nodeList.item(0).hasChildNodes();
    }

    private static void xml(Node node, Map<String, String> map) {
        String name = node.getNodeName();
        if (node.getAttributes() != null) {
            for (int i = 0; i < node.getAttributes().getLength(); i++) {
                xml(node.getAttributes().item(i), map);
            }
        }
        if (!isLeaf(node)) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                xml(node.getChildNodes().item(i), map);
            }
            return;
        }
        if (OdtNames.get(name) == null) {
            //System.err.println("Odt: неизвестное имя " + name);
            return;
        }
        map.put(OdtNames.get(name), node.getTextContent());
    }
}
