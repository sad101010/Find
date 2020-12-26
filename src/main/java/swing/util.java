package swing;

import java.awt.*;
import static java.lang.Math.max;
import static java.lang.Math.min;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class util {

    public static void append_right(Component what, Component to) {
        setxy(what, to.getX() + to.getWidth() + 3, to.getY());
    }

    public static void append_bottom(Component what, Component to) {
        setxy(what, to.getX(), to.getY() + to.getHeight() + 3);
    }

    public static void append_middle_bottom(Component what, Component to) {
        append_bottom(what, to);
        setxy(what, to.getX() + to.getWidth() / 2 - what.getWidth() / 2, what.getY());
    }

    public static void append_right_bottom(Component what, Component to) {
        append_bottom(what, to);
        setxy(what, to.getX() + to.getWidth() - what.getWidth(), what.getY());
    }

    public static void setxy(Component what, int x, int y) {
        Rectangle r = what.getBounds();
        what.setBounds(x, y, r.width, r.height);
    }

    public static void setsize(Component what, int width, int height) {
        Rectangle r = what.getBounds();
        what.setBounds(r.x, r.y, width, height);
    }

    public static void fit(Component what) {
        Dimension dim = what.getPreferredSize();
        setsize(what, dim.width, dim.height);
    }

    public static void window_to_center(Window w) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = dim.width / 2 - w.getWidth() / 2;
        int y = dim.height / 2 - w.getHeight() / 2;
        w.setBounds(x, y, w.getWidth(), w.getHeight());
    }

    public static void window_to_center(JFileChooser w) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = dim.width / 2 - w.getWidth() / 2;
        int y = dim.height / 2 - w.getHeight() / 2;
        w.setBounds(x, y, w.getWidth(), w.getHeight());
    }

    public static void window_to_center(Window what, Window to) {
        if (to == null) {
            window_to_center(what);
            return;
        }
        int x = to.getX() + to.getWidth() / 2 - what.getWidth() / 2;
        int y = to.getY() + to.getHeight() / 2 - what.getHeight() / 2;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        x = min(x, screen.width - what.getWidth());
        y = min(y, screen.height - what.getHeight());
        x = max(x, 0);
        y = max(y, 0);
        setxy(what, x, y);
    }

    public static void window_to_center(JFileChooser what, Window to) {
        if (to == null) {
            window_to_center(what);
            return;
        }
        int x = to.getX() + to.getWidth() / 2 - what.getWidth() / 2;
        int y = to.getY() + to.getHeight() / 2 - what.getHeight() / 2;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        x = min(x, screen.width - what.getWidth());
        y = min(y, screen.height - what.getHeight());
        x = max(x, 0);
        y = max(y, 0);
        setxy(what, x, y);
    }

    public static int packColumn(JTable table, int vColIndex, int margin) {

        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
        TableColumn col = colModel.getColumn(vColIndex);

        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }

        Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
        int width = comp.getPreferredSize().width;
        for (int r = 0; r < table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, vColIndex);
            comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, vColIndex), false, false, r, vColIndex);
            width = Math.max(width, comp.getPreferredSize().width);
        }

        width += margin;
        col.setPreferredWidth(width);

        return width + margin * 2;
    }

    public static int pack_columns(JTable table) {
        int width = 0;
        for (int k = 0; k < table.getColumnCount(); k++) {
            width += packColumn(table, k, 5);
        }
        return width;
    }

    public static void pop_dialog(useful_container __container, dialog __dialog) {
        __dialog.setContentPane(__container);
        __dialog.fit(3);
        window_to_center(__dialog, __dialog.getOwner());
        __dialog.setVisible(true);
    }
}
