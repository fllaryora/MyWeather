package training.globant.myweather.data.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import java.util.Date;
import java.util.List;


@Entity(tableName = "forecast_table")
public class Forecast {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  private long id;

  @ForeignKey(entity = Query.class, parentColumns = "id", childColumns = "queryId")
  private int queryId;

  private String cityLabel;
  private String countryLabel;

  @Ignore
  private List<ForecastItem> forecastItemList;

  //last updated
  private Date lastRefresh;

}
