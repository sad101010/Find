package com.mycompany;

import static util.util.err_msg;
import static util.util.exit_with_err_msg;
import static util.util.yn_promt;
import static util.directories.dirClear;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import swing.dialog;
import util.breader;

import javax.swing.*;
import util.thumbsdb;
import static util.xml.AddDocxTags;

public class Find {

    public static File MainCatalog, imgDir;
    public static threads.mainFindThread MainFindThread;
    public static final thumbsdb ThumbsDB = new thumbsdb();

    static {
        Map<String, String> map = new TreeMap<>();
        System.out.println("AddDocxTags: " + AddDocxTags(new File("docx.docx"), map));
        for (Map.Entry<String, String> e : map.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }
        /*try {
            System.setErr(new PrintStream(new FileOutputStream(new File("data/err.txt"))));
            System.setOut(new PrintStream(new FileOutputStream(new File("data/out.txt"))));
        } catch (Exception | Error e) {
        }*/
        try {
            Class.forName("meta.type");
        } catch (Exception | Error e) {
            exit_with_err_msg("Ошибка загрузки meta.type", null);
        }
        breader reader = null;
        try {
            reader = new breader("data/uimanager.txt");
        } catch (Exception | Error e) {
            exit_with_err_msg("Ошибка чтения uimanager", null);
        }
        while (true) {
            String line1 = null;
            String line2 = null;
            try {
                line1 = reader.readLine();
                line2 = reader.readLine();
            } catch (Exception | Error e) {
                exit_with_err_msg("Ошибка чтения базы", null);
            }
            if (line2 == null) {
                break;
            }
            UIManager.put(line1, line2);
        }
        try {
            reader.close();
        } catch (Exception | Error e) {
            err_msg("Ошибка закрытия breader", null);
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception | Error e) {
            err_msg("Ошибка получения системной темы", null);
        }
    }

    public static void main(final String[] args) {
        final dialog __dialog = new dialog(null, "find");
        ImageIcon icon = new ImageIcon("data/www.apache.org/hand.right.png");
        __dialog.setIconImage(icon.getImage());
        __dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        __dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (yn_promt("Выйти?", __dialog) == 0) {
                    dirClear("data/thumbnails");
                    if (MainFindThread != null && MainFindThread.isAlive()) {
                        MainFindThread.kill();
                    }
                    __dialog.dispose();
                }
            }
        });
        //pop_dialog(new crit(__dialog), __dialog);
    }
}
/*
Нельзя проверять Map.containsKey(null), иначе будет Exception
String s="111"; s.equals(null) можно делать
 */
