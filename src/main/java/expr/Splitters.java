package expr;

import static expr.util.issign;
import static expr.util.rm_whitespaces_from_ends;
import java.util.ArrayList;

public class Splitters {

    static ArrayList<String> operator_split(String expr) {
        ArrayList<String> list = new ArrayList();
        for (String s : expr.split("\\|")) {
            for (String t : s.split("&")) {
                list.add(t);
                list.add("&");
            }
            list.remove(list.size() - 1);
            list.add("|");
        }
        list.remove(list.size() - 1);
        return list;
    }

    static ArrayList<String> sign_split(String comparison) {
        ArrayList result = new ArrayList();
        int i = comparison.indexOf('"');
        if (i == -1) {
            return null;
        }
        int j = comparison.indexOf('"', i + 1);
        if (j == -1) {
            return null;
        }
        result.add(comparison.substring(i + 1, j));
        comparison = comparison.substring(j + 1);
        for (i = 0; i < comparison.length(); i++) {
            if (issign(comparison.charAt(i))) {
                break;
            }
        }
        if (i == comparison.length()) {
            return null;
        }
        result.add(comparison.charAt(i));
        result.add(rm_whitespaces_from_ends(comparison.substring(i + 1)));
        return result;
    }
    
}
