package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class xml {

    public static void load_xml(InputStream inputStream) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                System.out.println("+++++++++++++++++++");
                System.out.println(node.getNodeName());
                System.out.println("-------------------");
                System.out.println(node.getTextContent());
                System.out.println("===================");
            }
        } catch (Exception | Error e) {
        }
    }

    public static void doZip() throws IOException {
        ZipFile zip = new ZipFile("docx.docx");
        for (Enumeration e = zip.entries(); e.hasMoreElements();) {
            ZipEntry entry = (ZipEntry) e.nextElement();
            if (entry.isDirectory()) {
                continue;
            }
            switch(entry.getName()){
                case "docProps/core.xml":
                case "docProps/app.xml":
                    break;
                default:
                    continue;
            }
            System.out.println("-->" + entry.getName());
            if (FilenameUtils.getExtension(entry.getName()).equals("xml")) {
                load_xml(zip.getInputStream(entry));
            }
        }
    }

    private static StringBuilder getTxtFiles(InputStream in) {
        StringBuilder out = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
        } catch (IOException e) {
        }
        return out;
    }
}
