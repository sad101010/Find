package containers;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import static meta.FAttr.get_file_attr;
import swing.useful_container;
import swing.button.png_roll;
import swing.button.transparent_button;
import static swing.util.pack_columns;
import static swing.util.setxy;
import swing.dialog;
import static swing.util.append_bottom;
import static swing.util.append_right;
import static swing.util.setsize;
import static util.util.err_msg;

public class view extends useful_container {

    private final String normal[][];
    private final String fattr[][];
    private final transparent_button O = new transparent_button();
    private final transparent_button file_open_button = new transparent_button();
    private final png_roll FM = new png_roll();
    private final DefaultTableModel model = new DefaultTableModel();
    private final JTable table = new JTable(model);
    private final JScrollPane scrollPane = new JScrollPane(table);
    private File file;

    {
        add(scrollPane);

        O.setIcon(new ImageIcon("data/www.apache.org/folder.png"));
        O.setToolTipText("Открыть папку с файлом");
        O.addActionListener(actionEvent -> {
            try {
                //медленно, можно поправить
                Runtime.getRuntime().exec(System.getProperty("os.name").startsWith("Windows") ? new String[]{"explorer.exe", "/select", ","} : new String[]{"nautilus", "--select", file.getCanonicalPath()});
            } catch (Exception | Error e) {
                err_msg("Ошибка", owner);
            }
        });
        add(O);

        file_open_button.setIcon(new ImageIcon("data/www.apache.org/open_file.png"));
        file_open_button.setToolTipText("Открыть файл");
        file_open_button.addActionListener(actionEvent -> {
            try {
                Desktop.getDesktop().open(file);
            } catch (Exception | Error e) {
                err_msg("Ошибка", owner);
            }
        });
        add(file_open_button);

        FM.icons.add(new ImageIcon("data/www.apache.org/f.png"));
        FM.icons.add(new ImageIcon("data/www.apache.org/quill.png"));
        FM.tooltips.add("Файловые атрибуты");
        FM.tooltips.add("Метаданные");
        FM.setState(0);
        FM.addActionListener(actionEvent -> {
            FM.__switch(1);
            choose_table();
            owner.fit(3);
        });
        add(FM);

        table.getTableHeader().setResizingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setAutoCreateRowSorter(true);

        setxy(O, 3, 3);
        append_bottom(scrollPane, O);
        append_right(file_open_button, O);
        append_right(FM, file_open_button);
        setsize(scrollPane, 500, 300);
        //убрать фокусы, плохо смотрится
        for (Component c : getComponents()) {
            c.setFocusable(false);
        }
    }

    private static String[][] meta_to_table(Map<String, String> meta) {
        String[][] data = new String[meta.size()][2];
        int i = 0;
        for (Map.Entry<String, String> e : meta.entrySet()) {
            data[i++] = new String[]{e.getKey(), e.getValue()};
        }
        return data;
    }

    private void choose_table() {
        String[][] data = FM.getState() == 1 ? normal : fattr;
        model.setDataVector(data, new String[]{"Поле", "Значение"});
        //почему scrollPane а не таблица?
        setsize(scrollPane, Math.min(Math.max(pack_columns(table), 500), Toolkit.getDefaultToolkit().getScreenSize().width - 64), 300);
    }

    public view(File file, dialog owner) {
        super(owner);
        this.file = file;
        fattr = meta_to_table(get_file_attr(file));
        normal = meta_to_table(meta.meta.get_meta_for_user(file));
        choose_table();
    }
}
