package training.globant.myweather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francisco on 01/05/18.
 */

public class SkyDescription {

  @SerializedName("id")
  private int id;

  @SerializedName("main")
  private String name;

  @SerializedName("description")
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
