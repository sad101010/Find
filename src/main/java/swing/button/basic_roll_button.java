package swing.button;

import java.util.ArrayList;

public class basic_roll_button extends transparent_button {

    public boolean clickable = true;
    public final ArrayList<String> tooltips = new ArrayList();
    private int state = 0;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        setToolTipText(tooltips.get(state));
    }

    public void __switch(int offset) {
        if (!clickable) {
            return;
        }
        int new_state = (state + offset) % tooltips.size();
        setState((new_state + tooltips.size()) % tooltips.size());
    }
}
