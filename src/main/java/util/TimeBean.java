package util;

import java.time.Duration;
import java.util.Date;

public class TimeBean {

    private long sec;
    private long min;
    private final long hour;

    public long getSec() {
        return sec;
    }

    public long getMin() {
        return min;
    }

    public long getHour() {
        return hour;
    }

    private TimeBean(long seconds) {
        sec = seconds;
        min = sec / 60;
        sec %= 60;
        hour = min / 60;
        min %= 60;
    }
    
    public static TimeBean valueOf(long seconds){
        if(seconds<0){
            return null;
        }
        return new TimeBean(seconds);
    }
    
    public static TimeBean valueOf(String s){
        try{
            String t[] = s.split(":");
            if (t.length < 2 || t.length > 3) {
                return null;
            }
            long __hour = Long.valueOf(t[0]);
            long __min = Long.valueOf(t[1]);
            long __sec=t.length > 2?Long.valueOf(t[2]):0;
            return new TimeBean(__hour*3600+__min*60+__sec);
        }catch(Exception|Error e){
            return null;
        }
    }

    @Override
    public String toString() {
        String s1 = sec < 10 ? "0" + sec : "" + sec;
        String s2 = min < 10 ? "0" + min : "" + min;
        String s3 = hour < 10 ? "0" + hour : "" + hour;
        return s3 + ":" + s2 + ":" + s1;
    }

    public int compareTo(TimeBean timeBean) {
        if (hour < timeBean.getHour()) {
            return -1;
        }
        if (hour > timeBean.getHour()) {
            return 1;
        }
        if (min < timeBean.getMin()) {
            return -1;
        }
        if (min > timeBean.getMin()) {
            return 1;
        }
        if (sec < timeBean.getSec()) {
            return -1;
        }        
        if (sec > timeBean.getSec()) {
            return 1;
        }
        return 0;
    }

    public static String try_parse_doucument_edit_time(String time, String mime) {
        switch (mime) {
            case "application/msword":
            case "application/vnd.ms-excel":
            case "application/vnd.oasis.opendocument.spreadsheet":
            case "application/vnd.oasis.opendocument.text":
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
            case "application/rtf":
                break;
            default:
                return time;
        }
        String s = expr.util.rm_whitespaces_from_ends(time);
        if (s.startsWith("PT")) {
            Duration dur = Duration.parse(s);
            TimeBean timeBean = new TimeBean(dur.toMillis()/1000);
            return timeBean.toString();
        }
        if (time.isEmpty()) {
            //System.err.println("emtpy time! " + mime);
            return time;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isDigit(c)) {
                return time;
            }
        }
        Date date;
        switch (mime) {
            case "application/msword":
            case "application/vnd.ms-excel":
                if (s.endsWith("0000000")) {
                    s = s.substring(0, s.length() - 7);
                }
                return new TimeBean(Long.valueOf(s)).toString();
            default:
                return new TimeBean(Long.valueOf(s) * 60).toString();
        }
    }
}
