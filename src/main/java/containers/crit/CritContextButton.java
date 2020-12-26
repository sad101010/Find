package containers.crit;

import containers.context_container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import swing.button.transparent_button;
import swing.dialog;
import static swing.util.pop_dialog;

public class CritContextButton extends transparent_button {

    private final Window owner;

    public CritContextButton(Window owner) {
        this.owner = owner;
        setToolTipText("Текстовое содержимое документа");
        setIcon(new ImageIcon("data/www.apache.org/text.png"));
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clicked();
            }
        });
    }

    private void clicked() {
        dialog __dialog = new dialog(owner, "Контекст");
        pop_dialog(new context_container(__dialog), __dialog);
    }
}
