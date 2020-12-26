package expr;

import static expr.analyze.analyze;
import static expr.expr.polish_notation;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.ArrayList;
import static meta.Tika.get_mime;
import static meta.meta.getSmartMeta;
import util.DateBean;
import util.TimeBean;

import static util.img.loadBufferedImage;
import static util.img.bufferedImagesEqual;
import static util.util.stringIsBlank;

public class check {

    public static String text, mime;
    static ArrayList expr;
    private static Map meta;

    public static boolean initExpr(String userText) {
        //true, если удачно инициализровалось
        //false, иначе
        if (stringIsBlank(userText)) {
            expr = null;
        } else {
            expr = polish_notation(userText);
            if (expr == null) {
                return false;
            }
            System.out.println("after polish_notation:");
            for (int i = 0; i < check.expr.size(); i++) {
                System.out.print("{" + String.valueOf(check.expr.get(i)) + "} ");
            }
            System.out.println();
        }
        analyze();
        return true;
    }

    private static boolean compareExpression(Object value, char sign, Object userValue) {
        int comparison_result;
        if (userValue instanceof DateBean) {
            DateBean dateValue = DateBean.valueOf((String) value);
            comparison_result = dateValue.compareTo((DateBean) userValue);
        } else if (userValue instanceof Long) {
            Long longValue = Long.valueOf((String) value);
            comparison_result = longValue.compareTo((Long) userValue);
        } else if (userValue instanceof TimeBean) {
            TimeBean timeValue = TimeBean.valueOf((String) value);
            comparison_result = timeValue.compareTo((TimeBean) userValue);
        } else {
            String stringValue = (String) value;
            comparison_result = stringValue.compareTo((String) userValue);
        }
        switch (sign) {
            case '<':
                return comparison_result < 0;
            case '≤':
                return comparison_result <= 0;
            case '=':
                return comparison_result == 0;
            case '>':
                return comparison_result > 0;
            case '≥':
                return comparison_result >= 0;
            default:
                return false;
        }
    }

    private static boolean check(int i) {
        ArrayList expression = (ArrayList) expr.get(i);
        String field = (String) expression.get(0);
        Object value = meta.get(field);
        if (value == null) {
            //проверено, поле может быть null
            return false;
        }
        char sign = (char) expression.get(1);
        Object userValue = expression.get(2);
        if (field.equals("Изображения")) {
            for (Object o : (ArrayList) value) {
                BufferedImage img1 = (BufferedImage) o;
                BufferedImage img2 = loadBufferedImage(new File((String) userValue));
                if (bufferedImagesEqual(img1, img2)) {
                    return true;
                }
            }
            return false;
        }
        if (sign == '⊃') {
            return value.toString().contains(userValue.toString());
        }
        return compareExpression(value, sign, userValue);
    }

    private static boolean brute(int i) {
        if (expr.get(i) instanceof ArrayList) {
            return check(i);
        }
        String s = (String) expr.get(i);
        boolean b = brute(i - 2);
        return s.equals("&") ? b ? brute(i - 1) : false : b ? true : brute(i - 1);
    }

    public static boolean check(File file) {
        if (check.mime != null && !check.mime.equals(get_mime(file))) {
            return false;
        }
        meta = getSmartMeta(file);
        if (meta == null) {
            return false;
        }
        if (check.text != null) {
            String extracted_text = (String) meta.get("Внутренний текст");
            if (extracted_text == null || !extracted_text.contains(check.text)) {
                return false;
            }
        }
        if (expr == null) {
            return true;
        } else {
            return brute(expr.size() - 1);
        }
    }
}
