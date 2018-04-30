package training.globant.myweather.presentation.show_weather.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *  UI model of a Weather.
 * Parcelable data to transfer between fragments and to save/restore on orientation changes.
 * Created by francisco on 29/04/18.
 */

public class WeatherUI implements Parcelable {

    private String cityLabel;
    private String temperatureLabel;
    private String skyLabel;

    public WeatherUI(String cityLabel, String temperatureLabel, String skyLabel) {
        this.cityLabel = cityLabel;
        this.temperatureLabel = temperatureLabel;
        this.skyLabel = skyLabel;
    }

    public String getCityLabel() {
        return cityLabel;
    }

    public String getTemperatureLabel() {
        return temperatureLabel;
    }

    public String getSkyLabel() {
        return skyLabel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected WeatherUI(Parcel in) {
        cityLabel = in.readString();
        temperatureLabel = in.readString();
        skyLabel = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cityLabel);
        parcel.writeString(temperatureLabel);
        parcel.writeString(skyLabel);
    }

    public static final Creator<WeatherUI> CREATOR = new Creator<WeatherUI>() {
        @Override
        public WeatherUI createFromParcel(Parcel in) {
            return new WeatherUI(in);
        }

        @Override
        public WeatherUI[] newArray(int size) {
            return new WeatherUI[size];
        }
    };
}
