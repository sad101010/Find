package expr;

import java.util.ArrayList;
import static meta.FAttr.FAttrFields;

public class analyze {

    private static boolean __need_metadata, __need_fs_attributes, __need_img;

    public static boolean need_metadata() {
        return __need_metadata;
    }

    public static boolean need_fs_attributes() {
        return __need_fs_attributes;
    }

    public static boolean need_img() {
        return __need_img;
    }

    private static void check(int i) {
        ArrayList v = (ArrayList) check.expr.get(i);
        String field_name = (String) v.get(0);
        for (String s : FAttrFields) {
            if (s.equals(field_name)) {
                __need_fs_attributes = true;
                return;
            }
        }
        if (field_name.equals("Изображения")) {
            __need_img = true;
            return;
        }
        __need_metadata = true;
    }

    private static void brute(int i) {
        if (check.expr.get(i) instanceof String) {
            brute(i - 1);
            brute(i - 2);
            return;
        }
        check(i);
    }

    static void analyze() {
        __need_fs_attributes = false;
        __need_metadata = false;
        __need_img = false;
        if (check.expr == null) {
            return;
        }
        brute(check.expr.size() - 1);
    }
}
