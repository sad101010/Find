package swing.button;

import java.util.ArrayList;

public class text_roll extends basic_roll_button {

    public final ArrayList<String> texts = new ArrayList();

    @Override
    public void setState(int state) {
        super.setState(state);
        setText(texts.get(getState()));
    }
}
