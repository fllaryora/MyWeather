package training.globant.myweather.presentation.show_forecast.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import training.globant.myweather.R;
import training.globant.myweather.presentation.show_forecast.model.ForecastItemUI;

/**
 * Adapts a forecast View model in a Recyclelist
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyViewHolder> {

  private LayoutInflater mInflater;
  private List<ForecastItemUI> list;
  private Context context;

  /**
   * Constructor. data is passed into the constructor
   *
   * @param context of application
   * @param list of forecast, predictions
   */
  public ForecastAdapter(Context context, List<ForecastItemUI> list) {
    this.list = list;
    this.mInflater = LayoutInflater.from(context);
    this.context = context;
  }

  /**
   * Inflates the row layout from xml when needed.
   *
   * @param parent The ViewGroup into which the new View will be added after it is bound to an
   * adapter position.
   * @param viewType The view type of the new View.
   * @return A new ViewHolder that holds a View of the given view type.
   */
  @Override
  public ForecastAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = mInflater.inflate(R.layout.recycleview_forecast_item, parent, false);
    return new MyViewHolder(view);
  }

  /**
   * Called by RecyclerView to display the data at the specified position (i.e. row). This method
   * should update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
   * position.
   *
   * @param holder The ViewHolder which should be updated to represent the contents of the item at
   * the given position in the data set.
   * @param position The position of the item within the adapter's data set.
   */
  @Override
  public void onBindViewHolder(ForecastAdapter.MyViewHolder holder, int position) {
    ForecastItemUI forecastItemUI = list.get(position);
    holder.dayLabel.setText(forecastItemUI.getDayLabel());
    holder.hourLabel.setText(forecastItemUI.getHourLabel());

    String maxTemp = String
        .format(context.getString(R.string.temperature_template),
            forecastItemUI.getMaxTemperatureLabel());
    String minTemp = String
        .format(context.getString(R.string.temperature_template),
            forecastItemUI.getMinTemperatureLabel());
    String currTemp = String
        .format(context.getString(R.string.temperature_template),
            forecastItemUI.getTemperatureLabel());

    holder.temperatureLabel.setText(currTemp);
    holder.minTemperatureLabel.setText(minTemp);
    holder.maxTemperatureLabel.setText(maxTemp);

    holder.refreshImageView.setImageResource(forecastItemUI.getIcon());
  }

  /**
   * Returns the total number of items in the data set held by the adapter.
   *
   * @return The total number of items in this adapter.
   */
  @Override
  public int getItemCount() {
    return this.list.size();
  }

  /**
   * Create the holder for recycleview_forecast_item
   */
  public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView dayLabel, hourLabel, temperatureLabel, minTemperatureLabel, maxTemperatureLabel;
    public ImageView refreshImageView;

    public MyViewHolder(View view) {
      super(view);
      refreshImageView = (ImageView) view.findViewById(R.id.refreshImageView);
      dayLabel = (TextView) view.findViewById(R.id.dayLabel);
      hourLabel = (TextView) view.findViewById(R.id.hourLabel);
      temperatureLabel = (TextView) view.findViewById(R.id.temperatureLabel);
      minTemperatureLabel = (TextView) view.findViewById(R.id.minTemperatureLabel);
      maxTemperatureLabel = (TextView) view.findViewById(R.id.maxTemperatureLabel);
    }
  }
}
