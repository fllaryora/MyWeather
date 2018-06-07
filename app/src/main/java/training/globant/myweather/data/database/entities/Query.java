package training.globant.myweather.data.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Query {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  private long id;


}
