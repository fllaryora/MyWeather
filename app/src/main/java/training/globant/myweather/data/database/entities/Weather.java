package training.globant.myweather.data.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import java.util.Date;

@Entity(tableName = "weather_table")
public class Weather {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  private long id;

  @ForeignKey(entity = Query.class, parentColumns = "id", childColumns = "queryId")
  private int queryId;

  private String cityLabel;
  private String countryLabel;
  private String temperatureLabel;
  private String maxTemperatureLabel;
  private String minTemperatureLabel;
  private String skyLabel;
  private int icon;
  //last updated
  private Date lastRefresh;

}
