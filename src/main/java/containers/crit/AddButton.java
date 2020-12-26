package containers.crit;

import containers.expr.expr;
import expr.check;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import static meta.db.rusNames;
import swing.button.transparent_button;
import swing.dialog;
import swing.util;
import static util.util.err_msg;

public class AddButton extends transparent_button {

    private final Window owner;
    private final JTextArea area;

    public AddButton(Window owner, JTextArea area) {
        this.owner = owner;
        this.area = area;
        setIcon(new ImageIcon("data/www.apache.org/quill.png"));
        setToolTipText("Добавить условие метаданных");
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                add_clicked();
            }
        });
    }

    private void add_clicked() {
        if (check.mime == null) {
            err_msg("Тип файлов не выбран", owner);
            return;
        }
        dialog __dialog = new dialog(owner, "Метаданные");
        util.pop_dialog(new expr(area, rusNames(check.mime), __dialog), __dialog);
    }
}
