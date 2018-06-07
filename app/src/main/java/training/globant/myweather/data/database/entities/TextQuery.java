package training.globant.myweather.data.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class TextQuery {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "textSubmited")
  private String textSubmited;

  @ForeignKey(entity = Query.class, parentColumns = "id", childColumns = "queryId")
  private int queryId;


}
