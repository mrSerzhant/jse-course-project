package database.datamain.utils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateBuilder {
    private static Calendar calendar = Calendar.getInstance();
    private static DateFormat dateFormat = new SimpleDateFormat("HH:mm");

    public static String dateAsString(Date date) {
        return dateFormat.format(date);
    }

    public static Date createDate(String date) {
        String[] array = date.split("[:]");

        int years = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hour = Integer.parseInt(array[0]);
        int minute = Integer.parseInt(array[1]);

        calendar.set(years, month, day, hour, minute);
        return calendar.getTime();
    }

    public static String createDifferenceTime(Date firstTime, Date lastTime) {
        calendar.setTime(lastTime);
        long lastTimeMillis = calendar.getTimeInMillis();
        calendar.setTime(firstTime);
        long firstTimeMillis = calendar.getTimeInMillis();

        int minutesTimeMillis;

        if (lastTimeMillis < firstTimeMillis) {
            int differenceFromMidnight = (int) (86400000 - firstTimeMillis);
            minutesTimeMillis = (int) (differenceFromMidnight + lastTimeMillis) / 60000;

        } else {
            minutesTimeMillis = (int) (lastTimeMillis - firstTimeMillis) / 60000;
        }

        if (minutesTimeMillis < 60) {
            return String.format("0:%d", minutesTimeMillis);
        }

        int hours = minutesTimeMillis / 60;
        int minutes = minutesTimeMillis - hours * 60;

        return String.format("%d:%d", hours, minutes);
    }

    public static String printDate(Date date) {
        return dateFormat.format(date);
    }
}
