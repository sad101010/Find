package containers.crit;

import containers.mime_select;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import swing.button.transparent_button;
import swing.dialog;
import swing.util;

class MimeButton extends transparent_button {

    private final Window owner;

    public MimeButton(Window owner) {
        this.owner = owner;
        setIcon(new ImageIcon("data/www.apache.org/image2.png"));
        setToolTipText("Выбрать искомый тип файлов");
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clicked();
            }
        });
    }

    private void clicked() {
        dialog __dialog = new dialog(owner, "Тип файлов");
        util.pop_dialog(new mime_select(__dialog), __dialog);
    }
}
