package swing;

import java.awt.*;
import static java.lang.Math.max;

public class useful_container extends Container {

    public final dialog owner;

    public Dimension calc_size() {
        int w = 0, h = 0;
        for (Component c : getComponents()) {
            if (c.isVisible()) {
                w = max(w, c.getX() + c.getWidth());
                h = max(h, c.getY() + c.getHeight());
            }
        }
        return new Dimension(w, h);
    }

    public useful_container(dialog owner) {
        setLayout(null);
        this.owner = owner;
    }
}
