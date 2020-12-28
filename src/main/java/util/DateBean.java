package util;

import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateBean extends Date {

    private static final String formats[] = {
        "dd.MM.yyyy",
        "yyyy:MM:dd",
        "yyyy-MM-dd",
        "dd/MM/yyyy"
    };

    @Override
    public String toString() {
        DateFormat f = new SimpleDateFormat(formats[0]);
        return f.format(this);
    }

    public static DateBean MSFileTimeToDateBean(long _100Nanos) {
        //https://stackoverflow.com/questions/5200192/convert-64-bit-windows-number-to-time-java
        return new DateBean((_100Nanos - 116444736000000000L) / 10000);
    }

    public static DateBean valueOf(FileTime time) {
        if (time == null) {
            return null;
        }
        return new DateBean(time.toMillis());
    }

    public static DateBean valueOf(String string_date) {
        for (String s : formats) {
            SimpleDateFormat f = new SimpleDateFormat(s);
            try {
                return new DateBean(f.parse(string_date).getTime());
            } catch (Exception | Error e) {
            }
        }
        return null;
    }

    public static DateBean valueOf(Date date) {
        return new DateBean(date.getTime());
    }

    private DateBean(long milis) {
        super(milis);
    }
}
