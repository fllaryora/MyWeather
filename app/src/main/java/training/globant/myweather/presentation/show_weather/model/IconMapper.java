package training.globant.myweather.presentation.show_weather.model;
//https://github.com/ikromin/jphotoframe/blob/c687a1e4b536f15d457c30ffc8e26dd376d8b9b0/src/main/java/net/igorkromin/jphotoframe/weather/WeatherConditionCodes.java
import training.globant.myweather.R;
//http://erikflowers.github.io/weather-icons/
public enum IconMapper {
  THUNDERSTORM_WITH_LIGHT_RAIN(200, R.drawable.wi_thunderstorm, "Thunder", "Thunder storm with light rain"),
  THUNDERSTORM_WITH_RAIN(201, R.drawable.wi_thunderstorm, "Thunder", "Thunder storm with rain"),
  THUNDERSTORM_WITH_HEAVY_RAIN(202, R.drawable.wi_thunderstorm, "Thunder", "Thunder storm with heavy rain"),
  LIGHT_THUNDERSTORM(210, R.drawable.wi_thunderstorm, "Thunder", "Light thunder storm"),
  THUNDERSTORM(211, R.drawable.wi_thunderstorm, "Thunder", "Thunder storm"),
  HEAVY_THUNDERSTORM(212, R.drawable.wi_thunderstorm, "Thunder", "Heavy thunder storm"),
  RAGGED_THUNDERSTORM(221, R.drawable.wi_thunderstorm, "Thunder", "Ragged thunder storm"),
  THUNDERSTORM_WITH_LIGHT_DRIZZLE(230, R.drawable.wi_thunderstorm, "Thunder", "Thunder storm with light drizzle"),
  THUNDERSTORM_WITH_DRIZZLE(231, R.drawable.wi_thunderstorm, "Thunder", "Thunder storm with drizzle"),
  THUNDERSTORM_WITH_HEAVY_DRIZZLE(232, R.drawable.wi_thunderstorm, "Thunder", "Thunder storm with heavy drizzle"),

  LIGHT_INTENSITY_DRIZZLE(300, R.drawable.wi_raindrops, "Drizzle", "Light drizzle"),
  DRIZZLE(301, R.drawable.wi_raindrops, "Drizzle", "Drizzle"),
  HEAVY_INTENSITY_DRIZZLE(302, R.drawable.wi_raindrops, "Drizzle", "Heavy drizzle"),
  LIGHT_INTENSITY_DRIZZLE_RAIN(310, R.drawable.wi_raindrops, "Drizzle", "Ligh drizzle and rain"),
  DRIZZLE_RAIN(311, R.drawable.wi_raindrops, "Drizzle", "Drizzle and rain"),
  HEAVY_INTENSITY_DRIZZLE_RAIN(312, R.drawable.wi_raindrops, "Drizzle", "Heavy drizzle and rain"),
  SHOWER_RAIN_DRIZZLE(313, R.drawable.wi_raindrops, "Drizzle", "Shower, rain and drizzle"),
  HEAVY_SHOWER_RAIN_DRIZZLE(314, R.drawable.wi_raindrops, "Drizzle", "Heavy shower, rain and drizzle"),
  SHOWER_DRIZZLE(321, R.drawable.wi_raindrops, "Drizzle", "Shower and drizzle"),

  LIGHT_RAIN(500, R.drawable.wi_rain, "Rain", "Light rain"),
  MODERATE_RAIN(501, R.drawable.wi_rain, "Rain", "Moderate rain"),
  HEAVY_INTENSITY_RAIN(502, R.drawable.wi_rain, "Rain", "Heavy rain"),
  VERY_HEAVY_RAIN(503, R.drawable.wi_rain, "Rain", "Very heavy rain"),
  EXTREME_RAIN(504, R.drawable.wi_rain, "Rain", "Extreme rain"),
  FREEZING_RAIN(511, R.drawable.wi_rain, "Rain", "Freezing rain"),
  LIGHT_INTENSITY_SHOWER_RAIN(520, R.drawable.wi_rain, "Rain", "Light shower rain"),
  SHOWER_RAIN(521, R.drawable.wi_rain, "Rain", "Shower rain"),
  HEAVY_INTENSITY_SHOWER_RAIN(522, R.drawable.wi_rain, "Rain", "Heavy shower rain"),
  RAGGED_SHOWER_RAIN(531, R.drawable.wi_rain, "Rain", "Ragged shower rain"),

  LIGHT_SNOW(600, R.drawable.wi_snow, "Snow", "Light snow"),
  SNOW(601, R.drawable.wi_snow, "Snow", "Snow"),
  HEAVY_SNOW(602, R.drawable.wi_snow, "Snow", "Heavy snow"),
  SLEET(611, R.drawable.wi_snow, "Snow", "Sleet"),
  SHOWER_SLEET(612, R.drawable.wi_snow, "Snow", "Shower and sleet"),
  LIGHT_RAIN_SNOW(615, R.drawable.wi_snow, "Snow", "Light rain and snow"),
  RAIN_SNOW(616, R.drawable.wi_snow, "Snow", "Rain and snow"),
  LIGHT_SHOWER_SNOW(620, R.drawable.wi_snow, "Snow", "Light shower and snow"),
  SHOWER_SNOW(621, R.drawable.wi_snow, "Snow", "Shower and snow"),
  HEAVY_SHOWER_SNOW(622, R.drawable.wi_snow, "Snow", "Heavy shower and snow"),

  MIST(701, R.drawable.wi_fog, "Mist", "Mist"),
  SMOKE(711, R.drawable.wi_smoke, "Smoke", "Smoke"),
  HAZE(721, R.drawable.wi_day_haze, "Haze", "Haze"),
  SAND_OR_DUST_WHIRLS(731, R.drawable.wi_dust, "Dust", "Sand or dust whirls"),
  FOG(741, R.drawable.wi_fog, "Fog", "Fog"),
  SAND(751, R.drawable.wi_dust, "Sand", "Sand"),
  DUST(761, R.drawable.wi_dust, "Dust", "Dust"),
  VOLCANIC_ASH(762, R.drawable.wi_dust, "Ash", "Volcanic ash"),
  TORNADO1(781, R.drawable.wi_tornado, "Tornado", "Tornado"),

  SKY_IS_CLEAR(800, R.drawable.wi_day_sunny, "Clear", "Clear sky"),
  FEW_CLOUDS(801, R.drawable.wi_cloudy, "Clouds", "Few clouds"),
  SCATTERED_CLOUDS(802, R.drawable.wi_cloudy, "Clouds", "Scattered clouds"),
  BROKEN_CLOUDS(803, R.drawable.wi_cloudy, "Clouds", "Broken clouds"),
  OVERCAST_CLOUDS(804, R.drawable.wi_cloudy, "Clouds", "Overcast clouds"),

  TORNADO2(900, R.drawable.wi_tornado, "Tornado", "Tornado"),
  TROPICAL_STORM(901, R.drawable.wi_storm_showers, "Storm", "Tropical storm"),
  HURRICANE(902, R.drawable.wi_hurricane, "Hurricane", "Hurricane"),
  COLD(903, R.drawable.wi_snowflake_cold, "Cold", "Extreme cold"),
  HOT(904, R.drawable.wi_hot, "Hot", "Extreme heat"),
  WINDY(905, R.drawable.wi_windy, "Wind", "Extreme wind"),
  HAIL(906, R.drawable.wi_hail, "Hail", "Extreme hail"),

  NOT_AVAILABLE(3200, R.drawable.wi_na, "N/A", "N/A"),

  NO_CONNECTION(-1, R.drawable.wi_na, "No Connection", "No Connection");

  int code;
  int icon;
  String info;
  String detailedInfo;

  IconMapper(int code, int icon, String info, String detailedInfo) {
    this.code = code;
    this.icon = icon;
    this.info = info;
    this.detailedInfo = detailedInfo;
  }

  public static IconMapper fromInt(int code) {

    for (IconMapper w : IconMapper.values()) {
      if (w.code == code) {
        return w;
      }
    }

    return NOT_AVAILABLE;
  }

  public String getInfoText() {
    return info;
  }

  public String getDetailedInfo() {
    return detailedInfo;
  }

  public int getIcon() {
    return icon;
  }

  public String toString() {
    return Character.toString((char) icon);
  }
}
