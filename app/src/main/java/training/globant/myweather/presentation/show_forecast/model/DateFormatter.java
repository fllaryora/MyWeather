package training.globant.myweather.presentation.show_forecast.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import training.globant.myweather.data.utils.Constant;

public class DateFormatter {

  public static String getDayOfWeekFormatted(long UTCDateTime) {
    Calendar cal = Calendar.getInstance();
    TimeZone tz = cal.getTimeZone();
    SimpleDateFormat formatter = new SimpleDateFormat(Constant.DAY_OF_WEEK_FORMAT_PATTERN,
        Locale.getDefault());
    formatter.setTimeZone(tz);
    return formatter.format(new Date(UTCDateTime * 1000));
  }

  public static String getHourFormatted(long UTCDateTime) {
    Calendar cal = Calendar.getInstance();
    TimeZone tz = cal.getTimeZone();
    SimpleDateFormat formatter = new SimpleDateFormat(Constant.HOURS_IN_24_FORMAT_PATTERN,
        Locale.getDefault());
    formatter.setTimeZone(tz);
    return formatter.format(new Date(UTCDateTime * 1000));
  }
}
