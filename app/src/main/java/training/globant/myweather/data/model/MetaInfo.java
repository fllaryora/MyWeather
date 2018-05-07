package training.globant.myweather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francisco on 01/05/18.
 */

public class MetaInfo {

  @SerializedName("id")
  private int id;

  @SerializedName("main")
  private String name;

  @SerializedName("description")
  private String description;

  @SerializedName("icon")
  private String icon;

  public MetaInfo() {
    this.id = 0;
    this.name = "";
    this.description = "";
    this.icon = "";
  }

  public MetaInfo(int id, String name, String description, String icon) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.icon = icon;
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

  public String getIcon() {
    return icon;
  }
}
