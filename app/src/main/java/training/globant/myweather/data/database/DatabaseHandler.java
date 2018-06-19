package training.globant.myweather.data.database;

import training.globant.myweather.data.utils.Constant;

import android.os.Handler;
import android.os.Message;

/**
 * It is in charge of put room operation in background
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */
public class DatabaseHandler implements Handler.Callback{

  private AppDatabase database;
  private Handler handler;
  private Callback callback;

  public interface Callback {
    void onDatabaseOperationFinished();
  }

  public DatabaseHandler(AppDatabase database, Callback callback) {
    this.database = database;
    handler = new Handler(this);
    this.callback = callback;
  }

  public void execute(final Runnable body) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        database.runInTransaction(body);
        handler.sendEmptyMessage(Constant.OPERATION_SUCCESSFUL);
      }
    }).start();
  }


  @Override
  public boolean handleMessage(Message msg) {
    if (callback != null) {
      if (Constant.OPERATION_SUCCESSFUL == msg.what) {
        callback.onDatabaseOperationFinished();
        return true;
      }
    }
    return false;
  }
}
