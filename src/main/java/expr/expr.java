package expr;

import static expr.Splitters.operator_split;
import static expr.Splitters.sign_split;
import static meta.type.parseFieldValue;
import java.util.Stack;
import java.util.ArrayList;

import static expr.util.isop;
import static expr.util.opcmp;

public class expr {

    public static ArrayList polish_notation(String userText) {
        ArrayList<String> list = operator_split(userText);
        if (list == null) {
            return null;
        }
        ArrayList result = new ArrayList();
        Stack<String> stack = new Stack();
        for (String s : list) {
            if (!isop(s)) {
                ArrayList tmp = parseExpression(s);
                if (tmp == null) {
                    return null;
                }
                result.add(tmp);
                continue;
            }
            while (!stack.isEmpty() && opcmp(stack.lastElement(), s) >= 0) {
                result.add(stack.pop());
            }
            stack.add(s);
        }
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    private static ArrayList parseExpression(String s) {
        ArrayList<String> tmp = sign_split(s);
        if (tmp == null) {
            return null;
        }
        ArrayList result = new ArrayList();
        result.add(tmp.get(0));
        result.add(tmp.get(1));
        String value = tmp.get(2);
        String subValue = value.substring(1, value.length() - 1);
        if (value.startsWith("[") && value.endsWith("]")) {
            try {
                int id = Integer.parseInt(subValue);
                result.add(com.mycompany.Find.ThumbsDB.getPathById(id));
                return result;
            } catch (Exception | Error e) {
                return null;
            }
        }
        Object parsedValue = parseFieldValue(tmp.get(0), subValue);
        if (parsedValue == null) {
            return null;
        }
        result.add(parsedValue);
        return result;
    }
}
