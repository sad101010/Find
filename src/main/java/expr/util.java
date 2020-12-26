package expr;

public class util {

    public static boolean issign(char c) {
        switch (c) {
            case '<':
            case '≤':
            case '=':
            case '>':
            case '≥':
            case '⊃':
                return true;
            default:
                return false;
        }
    }

    public static int opcmp(String op1, String op2) {
        if (op1.equals(op2)) {
            return 0;
        }
        if (op1.equals("|") && op2.equals("&")) {
            return -1;
        }
        return 1;
    }

    public static boolean isop(String s) {
        return s.equals("|") || s.equals("&");
    }

    public static boolean isop(char c) {
        return c == '|' || c == '&';
    }

    public static String rm_whitespaces_from_ends(String s) {
        int i, j;
        for (i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                break;
            }
        }
        if (i == s.length()) {
            return "";
        }
        for (j = s.length() - 1; j >= i; j--) {
            if (!Character.isWhitespace(s.charAt(j))) {
                break;
            }
        }
        return s.substring(i, j + 1);
    }
}
