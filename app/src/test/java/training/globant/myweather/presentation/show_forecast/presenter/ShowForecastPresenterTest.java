package training.globant.myweather.presentation.show_forecast.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import training.globant.myweather.data.WeatherCallback;
import training.globant.myweather.data.database.DatabaseHandler;
import training.globant.myweather.data.model.City;
import training.globant.myweather.data.model.CountryHolder;
import training.globant.myweather.data.model.ForecastInfo;
import training.globant.myweather.data.model.ForecastItem;
import training.globant.myweather.data.model.SkyDescription;
import training.globant.myweather.data.model.TemperatureInfo;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.net.WeatherAPIClient;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.device.sensors.location.PermissionsHelper;
import training.globant.myweather.domain.SearchForecastInteractor;
import training.globant.myweather.domain.SearchWeatherInteractor;
import training.globant.myweather.presentation.show_forecast.ShowForecastContract;
import training.globant.myweather.presentation.show_forecast.model.CityUI;
import training.globant.myweather.presentation.show_forecast.model.DateFormatter;
import training.globant.myweather.presentation.show_forecast.model.ForecastItemUI;
import training.globant.myweather.presentation.show_weather.ShowWeatherContract;
import training.globant.myweather.presentation.show_weather.model.IconMapper;
import training.globant.myweather.presentation.show_weather.model.TemperatureFormatter;
import training.globant.myweather.presentation.show_weather.model.WeatherUI;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit tests for {@link ShowForecastPresenter}.
 *
 * @author Francisco LLaryora
 */

public class ShowForecastPresenterTest {

  private static final String COUNTRY = "AR";
  private static final double TEMPERATURE = 15.5;
  private static final double MINIMUM = 5.5;
  private static final double MAXIMUM = 25.5;
  private static final String SKY_DESCRIPTION = "clear";
  private static final int SKY_ICON_ID = 800;
  private static final String CITY = "Córdoba";
  private static final String CUSTOM_QUERY = "Córdoba,AR";
  private static final String CUSTOM_ERROR = "Custom ERROR";
  private static final String BAD_QUERY = "R2";
  private static final String INVALID_QUERY_STRING = "invalid query string";
  private static final boolean THE_MODEL_IS_VALID = true;
  private static final boolean THE_MODEL_IS_INVALID = false;
  private static final String LAT = "-31.4201";
  private static final String LON = "-64.1888";

  @Mock
  private DatabaseHandler databaseHandler;

  @Mock
  private PermissionsHelper permissionsHelper;

  /**
   * This mocks are private members of presenter
   */
  @Mock
  private ShowForecastContract.View view;

  @Mock
  private SearchForecastInteractor searchForecastInteractor;

  private static ForecastInfo model;

  private static CityUI uiModel;
  /**
   * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
   * perform further actions or assertions on them.
   */
  @Captor
  private ArgumentCaptor<WeatherCallback> weatherCallbackArgumentCaptor;

  @InjectMocks
  private ShowForecastPresenter presenter;

  @Before
  public void setUp() throws Exception{
    // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
    // inject the mocks in the test the initMocks method needs to be called.
    MockitoAnnotations.initMocks(this);

    // ALWAYS attach the view
    presenter.attachView(view);

  }

  @After
  public void tearDown() {

  }

  @Test
  public void loadForecast_shouldShowForecast() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, CUSTOM_QUERY);

    // Given a stubbed model
    givenAStubbedModel();

    //when
    presenter.loadForecast(parameters);
    // Callback is captured and invoked
    when(view.getDatabaseHandler()).thenReturn(databaseHandler);
    verify(searchForecastInteractor).execute(anyMap(), weatherCallbackArgumentCaptor.capture());
    weatherCallbackArgumentCaptor.getValue().onResponse(model);
    verify(databaseHandler).execute(any(Runnable.class));
    //this is the callback of databaseHandler execute
    presenter.onDatabaseOperationFinished();
    // Then
    CityUI uiModel = presenter.getUiModel();
    verify(view).showForecast(uiModel);
  }

  @Test
  public void loadForecast_when_is_offline_shouldShowOffline() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, CUSTOM_QUERY);

    //when
    presenter.loadForecast(parameters);
    // Callback is captured and invoked
    when(view.getDatabaseHandler()).thenReturn(databaseHandler);
    verify(searchForecastInteractor).execute(anyMap(), weatherCallbackArgumentCaptor.capture());
    weatherCallbackArgumentCaptor.getValue().onOffline();

    verify(databaseHandler).execute(any(Runnable.class));
    //this is the callback of databaseHandler execute
    presenter.onDatabaseOperationFinished();
    // Then
    verify(view).showOffline();
  }

  @Test
  public void loadForecast_when_is_on_Error_shouldShowError() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, CUSTOM_QUERY);

    //when
    presenter.loadForecast(parameters);
    // Callback is captured and invoked
    verify(searchForecastInteractor).execute(anyMap(), weatherCallbackArgumentCaptor.capture());
    weatherCallbackArgumentCaptor.getValue().onError(CUSTOM_ERROR);

    //this is the callback of databaseHandler execute
    presenter.onDatabaseOperationFinished();
    // Then
    verify(view).showError(CUSTOM_ERROR);
  }

  @Test
  public void loadForecast_with_bad_query_shouldShowError() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, BAD_QUERY);

    //when
    when(view.getInvalidQueryString()).thenReturn(INVALID_QUERY_STRING);
    presenter.loadForecast(parameters);

    // Then
    verify(view).showError(INVALID_QUERY_STRING);
  }

  @Test
  public void loadForecast_with_GPS_request_shouldShowForecast() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();
    Map<String, String> parametersWithGPS = new HashMap<String, String>();
    parametersWithGPS.put(Constant.API_PARAMETER_LATITUDE, LAT);
    parametersWithGPS.put(Constant.API_PARAMETER_LONGITUDE, LON);
    // Given a stubbed model
    givenAStubbedModel();

    //when
    presenter.loadForecast(parameters);
    // Callback is captured and invoked
    when(view.getPermissionHelper()).thenReturn(permissionsHelper);
    when(view.getDatabaseHandler()).thenReturn(databaseHandler);
    verify(searchForecastInteractor).executeGPS(any(PermissionsHelper.class), weatherCallbackArgumentCaptor.capture());
    weatherCallbackArgumentCaptor.getValue().onGeolocation(parametersWithGPS);

    //normal path
    verify(searchForecastInteractor).execute(anyMap(), weatherCallbackArgumentCaptor.capture());
    weatherCallbackArgumentCaptor.getValue().onResponse(model);
    verify(databaseHandler).execute(any(Runnable.class));
    //this is the callback of databaseHandler execute
    presenter.onDatabaseOperationFinished();
    // Then
    CityUI uiModel = presenter.getUiModel();
    verify(view).showForecast(uiModel);
  }

  @Test
  public void restoreStateAndShowWeather_is_valid_shouldShowWeather() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, CUSTOM_QUERY);

    // Given a stubbed model
    givenAStubbedUIModel();

    //when
    presenter.restoreStateAndShowForecast(uiModel, THE_MODEL_IS_VALID);

    //then
    verify(view).showForecast(uiModel);

  }

  @Test
  public void restoreStateAndShowForecast_is_invalid_shouldNotCallShowForecast() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, CUSTOM_QUERY);

    // Given a stubbed model
    givenAStubbedUIModel();

    //when
    presenter.restoreStateAndShowForecast(uiModel, THE_MODEL_IS_INVALID);

    //then
    assertEquals(null,presenter.getUiModel());
    verify(view, never()).showForecast(uiModel);
  }

  @Test
  public void refreshForecast_shouldShowForecast() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, CUSTOM_QUERY);

    // Given a stubbed model
    givenAStubbedModel();

    //when
    presenter.refreshForecast(parameters);
    // Callback is captured and invoked
    when(view.getDatabaseHandler()).thenReturn(databaseHandler);
    verify(searchForecastInteractor).execute(anyMap(), weatherCallbackArgumentCaptor.capture());
    weatherCallbackArgumentCaptor.getValue().onResponse(model);
    verify(databaseHandler).execute(any(Runnable.class));
    //this is the callback of databaseHandler execute
    presenter.onDatabaseOperationFinished();
    // Then
    CityUI uiModel = presenter.getUiModel();
    verify(view).showForecast(uiModel);
  }

  @Test
  public void refreshForecast_without_parameters_shouldStopRefreshing() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();

    //when
    presenter.refreshForecast(parameters);

    // Then
    verify(view).stopRefreshing();
  }

  @Test
  public void transformModelToUiModel_shouldTransformDataProperly() {
    // Given a stubbed model
    givenAStubbedModel();

    // When
    CityUI localUiModel = presenter.transformModelToUiModel(model);

    // Then
    assertEquals(model.getCity().getCountryName(), localUiModel.getCountryLabel());
    assertEquals(model.getCity().getCityName(), localUiModel.getCityLabel());
    int icon = IconMapper.fromInt(model.getList().get(0).getSkyDescriptions().get(0).getId()).getIcon();
    assertEquals(icon, localUiModel.getForecastItemUIList().get(0).getIcon());


    String temp = TemperatureFormatter.getTemperatureFormatted(model.getList().get(0).getTemperatureInfo().getTemperature());
    assertEquals(temp, localUiModel.getForecastItemUIList().get(0).getTemperatureLabel());
    temp = TemperatureFormatter.getTemperatureFormatted(model.getList().get(0).getTemperatureInfo().getMaximum());
    assertEquals(temp, localUiModel.getForecastItemUIList().get(0).getMaxTemperatureLabel());
    temp = TemperatureFormatter.getTemperatureFormatted(model.getList().get(0).getTemperatureInfo().getMinimum());
    assertEquals(temp, localUiModel.getForecastItemUIList().get(0).getMinTemperatureLabel());

    String date = DateFormatter.getDayOfWeekFormatted(model.getList().get(0).getDateTime());
    assertEquals(date, localUiModel.getForecastItemUIList().get(0).getDayLabel());
    date = DateFormatter.getHourFormatted(model.getList().get(0).getDateTime());
    assertEquals(date, localUiModel.getForecastItemUIList().get(0).getHourLabel());

    }

  private void givenAStubbedModel() {
    List< SkyDescription > skyDescriptionList = new ArrayList< SkyDescription >();
    SkyDescription skyDescription = new SkyDescription(SKY_ICON_ID, "", SKY_DESCRIPTION);
    skyDescriptionList.add(skyDescription);
    TemperatureInfo temperatureInfo = new TemperatureInfo(TEMPERATURE, MINIMUM, MAXIMUM);
    List<ForecastItem> list = new ArrayList<>();
    list.add(new ForecastItem(System.currentTimeMillis(), temperatureInfo,  skyDescriptionList));
    City city = new City(CITY, COUNTRY);
    model = new ForecastInfo( city, list.size(),  list);
  }

  private void givenAStubbedUIModel() {
    givenAStubbedModel();
    uiModel = presenter.transformModelToUiModel(model);
  }

}
