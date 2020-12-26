package containers.crit;

import containers.expr.expr;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import static meta.FAttr.FAttrFields;
import swing.button.transparent_button;
import swing.dialog;
import static swing.util.pop_dialog;

public class CritFAttrButton extends transparent_button {

    private final Window owner;
    private final JTextArea area;

    public CritFAttrButton(Window owner, JTextArea area) {
        this.owner = owner;
        this.area = area;
        setToolTipText("Добавить условие файловых атрибутов");
        setIcon(new ImageIcon("data/www.apache.org/f.png"));
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                fs_attributes_clicked();
            }
        });
    }

    private void fs_attributes_clicked() {
        dialog __dialog = new dialog(owner, "Атрибуты файла");
        pop_dialog(new expr(area, FAttrFields, __dialog), __dialog);
    }
}
