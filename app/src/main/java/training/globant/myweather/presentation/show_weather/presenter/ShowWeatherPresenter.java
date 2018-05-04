package training.globant.myweather.presentation.show_weather.presenter;

import java.util.Map;

import training.globant.myweather.data.WeatherCallback;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.domain.SearchWeatherInteractor;
import training.globant.myweather.presentation.show_weather.ShowWeatherContract;
import training.globant.myweather.presentation.show_weather.model.WeatherUI;

/**
 * Presenter that handles user actions from {@link training.globant.myweather.presentation.show_weather.view.ShowWeatherFragment} view,
 * show....
 * <p>
 * Notice that Presenter knows nothing about Android framework.
 * Created by francisco on 29/04/18.
 */

public class ShowWeatherPresenter implements ShowWeatherContract.Presenter,WeatherCallback {

    private ShowWeatherPresenter(){}

    static private ShowWeatherPresenter myInstance = null;

    static public ShowWeatherPresenter getInstance(){
        if(myInstance == null){
            myInstance = new ShowWeatherPresenter();
        }
        return myInstance;
    }

    private ShowWeatherContract.View view;
    private WeatherUI uiModel;

    @Override
    public void attachView(ShowWeatherContract.View view) {
        this.view = view;
    }

    @Override
    public boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void loadWeather(Map<String,String> parameters) {
        SearchWeatherInteractor searchWeatherInteractor = new SearchWeatherInteractor();
        searchWeatherInteractor.execute(parameters,this);
    }

    @Override
    public WeatherUI transformModelToUiModel(WeatherInfo model) {
        //TODO change DEScription and add icons
        //https://openweathermap.org/weather-conditions
        String cityName = "No name";
        String description = "No description";
        String temperatureInfo = "No Temp";
        if(model != null){
            if( model.getMetaInfo() != null && model.getMetaInfo().size() > 0 ){
                description = model.getMetaInfo().get(0).getDescription();
            }

            if( model.getTemperatureInfo() != null ){
                temperatureInfo = model.getTemperatureInfo().getTemperature()+"";
            }
            if(model.getName() != null){
                cityName = model.getName();
            }
        }

        return new WeatherUI( cityName, temperatureInfo, description );
    }

    @Override
    public WeatherUI getUiModel() {
        return uiModel;
    }


    @Override
    public void onResponse(WeatherInfo weatherInfo) {
        if (isViewAttached()) {
            uiModel = transformModelToUiModel(weatherInfo);
            view.showWeather(uiModel);
        }
    }

    @Override
    public void onError(String error) {
        if (isViewAttached()) {
            view.showError(error);
        }
    }
}
