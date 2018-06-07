package training.globant.myweather.data.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

@Entity(primaryKeys={"latitude","longitude"})
public class GeolocationQuery {

  @NonNull
  @ColumnInfo(name = "latitude")
  private String latitude;
  @NonNull
  @ColumnInfo(name = "longitude")
  private String longitude;

  @ForeignKey(entity = Query.class, parentColumns = "id", childColumns = "queryId")
  private int queryId;

}
