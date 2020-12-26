package swing.button;

import java.util.ArrayList;
import javax.swing.*;

public class png_roll extends basic_roll_button {

    public final ArrayList<ImageIcon> icons = new ArrayList();

    @Override
    public void setState(int state) {
        super.setState(state);
        setIcon(icons.get(getState()));
    }
}
