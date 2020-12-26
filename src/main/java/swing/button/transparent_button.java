package swing.button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static swing.util.setsize;

public class transparent_button extends JButton {

    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setContentAreaFilled(true);
                setBorderPainted(true);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setContentAreaFilled(false);
                setBorderPainted(false);
            }
        });
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFont(new Font("Arial Unicode", Font.PLAIN, 14));
        setMargin(new Insets(0, 0, 0, 0));
        setsize(this, 30, 30);
    }
}
