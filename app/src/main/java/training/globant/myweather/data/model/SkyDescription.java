package training.globant.myweather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a json that describes the sky from rest service
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */
public class SkyDescription {

  private int id;

  @SerializedName("main")
  private String name;

  private String description;

  public SkyDescription() {
    this.id = 0;
    this.name = "";
    this.description = "";
  }

  public SkyDescription(int id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

}
