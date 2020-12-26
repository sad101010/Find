package swing;

import javax.swing.*;
import java.awt.*;

import static swing.util.*;
import static util.util.exit_with_err_msg;

public final class dialog extends JDialog {

    public static final Dimension decor = calc_decor();

    public dialog(Window parent, String title) {
        super(parent, title, parent == null ? ModalityType.MODELESS : ModalityType.APPLICATION_MODAL);
        setResizable(false);
        ImageIcon icon = new ImageIcon("data/www.apache.org/1px.png");
        setIconImage(icon.getImage());
    }

    private static Dimension calc_decor() {
        final JDialog dlg = new JDialog();
        dlg.setLayout(null);
        dlg.setResizable(false);
        JLabel label = new JLabel("Загрузка...");
        dlg.add(label);
        setxy(label, 3, 3);
        util.fit(label);
        setsize(dlg, label.getWidth() + 100, label.getHeight() + 100);
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    dlg.setVisible(true);
                }
            });
        } catch (Exception | Error e) {
            exit_with_err_msg("Ошибка инициализации декораций окон", null);
        }
        Dimension dim = get_decoration_size(dlg);
        dlg.dispose();
        return dim;
    }

    private static Dimension get_decoration_size(JDialog dlg) {
        Rectangle window = dlg.getBounds();
        Rectangle content = dlg.getContentPane().getBounds();
        int width = window.width - content.width;
        int height = window.height - content.height;
        return new Dimension(width, height);
    }

    public void fit(int padding) {
        useful_container c = (useful_container) getContentPane();
        Dimension d1 = c.calc_size();
        Dimension d2 = decor;
        int width = d1.width + d2.width + padding;
        int height = d1.height + d2.height + padding;
        setsize(this, width, height);
    }
}
