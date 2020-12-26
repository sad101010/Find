package util;

import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateBean {

    public static Date WindowsDate(long _100Nanos) {
        //https://stackoverflow.com/questions/5200192/convert-64-bit-windows-number-to-time-java
        return new Date((_100Nanos - 116444736000000000L) / 10000);
    }

    private static final String formats[] = {
        "dd.MM.yyyy HH:mm:ss",
        "yyyy:MM:dd",
        "yyyy-MM-dd",
        "dd/MM/yyyy"
    };

    public static String DateToString(Date date) {
        DateFormat f = new SimpleDateFormat(formats[0]);
        if (date == null) {
            return null;
        }
        return f.format(date);
    }

    public static String FileTimeToString(FileTime time) {
        if (time == null) {
            return null;
        }
        Date date = new Date(time.toMillis());
        return DateToString(date);
    }

    public static Date StringToDate(String string_date) {
        for (String s : formats) {
            SimpleDateFormat f = new SimpleDateFormat(s);
            try {
                return f.parse(string_date);
            } catch (Exception | Error e) {
            }
        }
        return null;
    }

    public static String TryParseDate(String s) {
        Date date = StringToDate(s);
        return date == null ? s : DateToString(date);
    }
}
