package training.globant.myweather.data.utils;

/**
 * Holds the constants of the project regardless main configurations
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class Constant {

  public static final String DB_NAME = "weather_database";
  public static final String DB_TABLE_WEATHER = "weather_table";
  public static final String DB_TABLE_FORECAST = "forecast_table";
  public static final String DB_TABLE_QUERY = "query_table";

  public static final String KEY_LAST_SEARCH = "KEY_LAST_SEARCH";
  public static final String KEY_WEATHER = "KEY_WEATHER";
  public static final String KEY_FORECAST = "KEY_FORECAST";

  public static final String KEY_WEATHER_MODEL_VALID = "KEY_WEATHER_MODEL_VALID";
  public static final String KEY_FORECAST_MODEL_VALID = "KEY_FORECAST_MODEL_VALID";
  public static final boolean INVALID = false;
  public static final boolean VALID = true;

  public static final String API_PARAMETER_QUERY = "q";
  public static final String API_PARAMETER_LATITUDE = "lat";
  public static final String API_PARAMETER_LONGITUDE = "lon";
  public static final String API_PARAMETER_APP_ID = "APPID";
  public static final String API_PARAMETER_TEMPETATURE_UNITS = "units";
  public static final String API_PARAMETER_LANG = "lang";
  public static final String API_VALUE_DEGREES_CELSIUS = "metric";
  public static final String API_VALUE_LANG_SPANISH = "es";
  public static final char DECIMAL_SEPARATOR = ',';
  public static final String DECIMAL_FORMAT_PATTERN = "##.#";
  public static final String DAY_OF_WEEK_FORMAT_PATTERN = "EEEE";
  public static final String HOURS_IN_24_FORMAT_PATTERN = "HH:mm";
  public static final String ERROR_MESSAGES_FORMAT = "%d %s";
  public static final int MIN_QUERY_LEN = 4;
}
