package training.globant.myweather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a json that describes the country name
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */
public class CountryHolder {

  @SerializedName("country")
  private String name;

  public CountryHolder(String name) {
    this.name = name;
  }

  public CountryHolder() {
    this.name = "";
  }

  public String getName() {
    return name;
  }
}
