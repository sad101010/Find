package util;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class util {

    public static String current_time() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(Calendar.getInstance().getTime());
    }

    public static void err_msg(String msg, Component parent) {
        JOptionPane.showMessageDialog(parent, msg, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public static void msg(String msg, Component parent) {
        JOptionPane.showMessageDialog(parent, msg, "Уведомление", JOptionPane.PLAIN_MESSAGE);
    }

    public static void exit_with_err_msg(final String msg, final Component parent) {
        err_msg(msg, parent);
        System.exit(-1);
    }

    public static int yn_promt(String msg, Component parent) {
        return JOptionPane.showOptionDialog(parent, msg, "Уведомление", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("data/icons/gnome/32x32/status/dialog-warning.png"), new Object[]{"Да", "Нет"}, "Да");
    }

    public static String getPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (Exception | Error e) {
            return null;
        }
    }

    public static boolean BadOrSymLink(File file) {
        try {
            //если файл из одной ФС был перемещен в другую
            //и возникла ошибка - нельзя использовать
            //некоторые символы в именах - try-catch
            if (Files.isSymbolicLink(file.toPath())) {
                //проверено: символические ссылки есть
                return true;
            }
        } catch (Exception | Error e) {
            return true;
        }
        return false;
    }

    public static boolean stringIsBlank(String s) {
        int i;
        for (i = 0; i < s.length(); i++) {
            if (Character.isWhitespace(s.charAt(i))) {
                break;
            }
        }
        return i == s.length();
    }
}
