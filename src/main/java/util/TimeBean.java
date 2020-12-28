package util;

public class TimeBean {

    private final long sec;
    private final long min;
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
        sec = seconds % 60;
        seconds /= 60;
        min = seconds % 60;
        seconds /= 60;
        hour = seconds % 60;
    }

    public static TimeBean valueOf(long seconds) {
        if (seconds < 0) {
            return null;
        }
        return new TimeBean(seconds);
    }

    public static TimeBean valueOf(String s) {
        try {
            String t[] = s.split(":");
            if (t.length < 2 || t.length > 3) {
                return null;
            }
            long __hour = Long.valueOf(t[0]);
            long __min = Long.valueOf(t[1]);
            long __sec = t.length > 2 ? Long.valueOf(t[2]) : 0;
            return new TimeBean(__hour * 3600 + __min * 60 + __sec);
        } catch (Exception | Error e) {
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
}
