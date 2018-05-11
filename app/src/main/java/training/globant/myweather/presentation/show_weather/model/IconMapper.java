package training.globant.myweather.presentation.show_weather.model;
//https://github.com/ikromin/jphotoframe/blob/c687a1e4b536f15d457c30ffc8e26dd376d8b9b0/src/main/java/net/igorkromin/jphotoframe/weather/WeatherConditionCodes.java

import training.globant.myweather.R;

//http://erikflowers.github.io/weather-icons/
public enum IconMapper {
  THUNDERSTORM_WITH_LIGHT_RAIN(200, R.drawable.wi_thunderstorm),
  THUNDERSTORM_WITH_RAIN(201, R.drawable.wi_thunderstorm),
  THUNDERSTORM_WITH_HEAVY_RAIN(202, R.drawable.wi_thunderstorm),
  LIGHT_THUNDERSTORM(210, R.drawable.wi_thunderstorm),
  THUNDERSTORM(211, R.drawable.wi_thunderstorm),
  HEAVY_THUNDERSTORM(212, R.drawable.wi_thunderstorm),
  RAGGED_THUNDERSTORM(221, R.drawable.wi_thunderstorm),
  THUNDERSTORM_WITH_LIGHT_DRIZZLE(230, R.drawable.wi_thunderstorm),
  THUNDERSTORM_WITH_DRIZZLE(231, R.drawable.wi_thunderstorm),
  THUNDERSTORM_WITH_HEAVY_DRIZZLE(232, R.drawable.wi_thunderstorm),

  LIGHT_INTENSITY_DRIZZLE(300, R.drawable.wi_raindrops),
  DRIZZLE(301, R.drawable.wi_raindrops),
  HEAVY_INTENSITY_DRIZZLE(302, R.drawable.wi_raindrops),
  LIGHT_INTENSITY_DRIZZLE_RAIN(310, R.drawable.wi_raindrops),
  DRIZZLE_RAIN(311, R.drawable.wi_raindrops),
  HEAVY_INTENSITY_DRIZZLE_RAIN(312, R.drawable.wi_raindrops),
  SHOWER_RAIN_DRIZZLE(313, R.drawable.wi_raindrops),
  HEAVY_SHOWER_RAIN_DRIZZLE(314, R.drawable.wi_raindrops),
  SHOWER_DRIZZLE(321, R.drawable.wi_raindrops),

  LIGHT_RAIN(500, R.drawable.wi_rain),
  MODERATE_RAIN(501, R.drawable.wi_rain),
  HEAVY_INTENSITY_RAIN(502, R.drawable.wi_rain),
  VERY_HEAVY_RAIN(503, R.drawable.wi_rain),
  EXTREME_RAIN(504, R.drawable.wi_rain),
  FREEZING_RAIN(511, R.drawable.wi_rain),
  LIGHT_INTENSITY_SHOWER_RAIN(520, R.drawable.wi_rain),
  SHOWER_RAIN(521, R.drawable.wi_rain),
  HEAVY_INTENSITY_SHOWER_RAIN(522, R.drawable.wi_rain),
  RAGGED_SHOWER_RAIN(531, R.drawable.wi_rain),

  LIGHT_SNOW(600, R.drawable.wi_snow),
  SNOW(601, R.drawable.wi_snow),
  HEAVY_SNOW(602, R.drawable.wi_snow),
  SLEET(611, R.drawable.wi_snow),
  SHOWER_SLEET(612, R.drawable.wi_snow),
  LIGHT_RAIN_SNOW(615, R.drawable.wi_snow),
  RAIN_SNOW(616, R.drawable.wi_snow),
  LIGHT_SHOWER_SNOW(620, R.drawable.wi_snow),
  SHOWER_SNOW(621, R.drawable.wi_snow),
  HEAVY_SHOWER_SNOW(622, R.drawable.wi_snow),

  MIST(701, R.drawable.wi_fog),
  SMOKE(711, R.drawable.wi_smoke),
  HAZE(721, R.drawable.wi_day_haze),
  SAND_OR_DUST_WHIRLS(731, R.drawable.wi_dust),
  FOG(741, R.drawable.wi_fog),
  SAND(751, R.drawable.wi_dust),
  DUST(761, R.drawable.wi_dust),
  VOLCANIC_ASH(762, R.drawable.wi_dust),
  TORNADO1(781, R.drawable.wi_tornado),

  SKY_IS_CLEAR(800, R.drawable.wi_day_sunny),
  FEW_CLOUDS(801, R.drawable.wi_cloudy),
  SCATTERED_CLOUDS(802, R.drawable.wi_cloudy),
  BROKEN_CLOUDS(803, R.drawable.wi_cloudy),
  OVERCAST_CLOUDS(804, R.drawable.wi_cloudy),

  TORNADO2(900, R.drawable.wi_tornado),
  TROPICAL_STORM(901, R.drawable.wi_storm_showers),
  HURRICANE(902, R.drawable.wi_hurricane),
  COLD(903, R.drawable.wi_snowflake_cold),
  HOT(904, R.drawable.wi_hot),
  WINDY(905, R.drawable.wi_windy),
  HAIL(906, R.drawable.wi_hail),

  NOT_AVAILABLE(3200, R.drawable.wi_na),

  NO_CONNECTION(-1, R.drawable.wi_na);

  int code;
  int icon;

  IconMapper(int code, int icon) {
    this.code = code;
    this.icon = icon;
  }

  public static IconMapper fromInt(int code) {

    for (IconMapper w : IconMapper.values()) {
      if (w.code == code) {
        return w;
      }
    }
    return NOT_AVAILABLE;
  }

  public int getIcon() {
    return icon;
  }

}
