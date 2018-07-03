package training.globant.myweather.presentation.show_weather.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import training.globant.myweather.data.WeatherCallback;
import training.globant.myweather.data.model.CountryHolder;
import training.globant.myweather.data.model.SkyDescription;
import training.globant.myweather.data.model.TemperatureInfo;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.device.sensors.location.PermissionsHelper;
import training.globant.myweather.domain.SearchWeatherInteractor;
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
 * Unit tests for {@link ShowWeatherPresenter}.
 *
 * @author Francisco LLaryora
 */

public class ShowWeatherPresenterTest {

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
  private PermissionsHelper permissionsHelper;

  /**
   * This mocks are private members of presenter
   */
  @Mock
  private ShowWeatherContract.View view;

  @Mock
  private SearchWeatherInteractor searchWeatherInteractor;

  private static WeatherInfo model;
  private static WeatherUI uiModel;
  /**
   * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
   * perform further actions or assertions on them.
   */
  @Captor
  private ArgumentCaptor<WeatherCallback> weatherCallbackArgumentCaptor;

  @InjectMocks
  private ShowWeatherPresenter presenter;

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
  public void loadWeather_withParams_shouldCallIteractor() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, CUSTOM_QUERY);

    //when
    presenter.loadWeather(parameters);

    //then
    verify(searchWeatherInteractor).execute(anyMap(), weatherCallbackArgumentCaptor.capture());
  }

  @Test
  public void loadWeather_withoutParams_shouldCallGPSIteractor() {
    // Given empty parameters
    Map<String, String> parameters = new HashMap<String, String>();

    //when
    presenter.loadWeather(parameters);

    //then
    verify(searchWeatherInteractor).executeGPS(any(PermissionsHelper.class) , weatherCallbackArgumentCaptor.capture());
  }

  @Test
  public void loadWeather_withInvalidParams_shouldShowWeather() {
    // Given invalid parameters
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, BAD_QUERY);

    //when
    when(view.getInvalidQueryString()).thenReturn(INVALID_QUERY_STRING);
    presenter.loadWeather(parameters);

    // Then
    verify(view).showError(INVALID_QUERY_STRING);
  }

  @Test
  public void refreshWeather_shouldShowWeather() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, CUSTOM_QUERY);
    //when
    presenter.refreshWeather(parameters);
    // Then
    verify(searchWeatherInteractor).execute(anyMap(), weatherCallbackArgumentCaptor.capture());
  }


  @Test
  public void refreshWeather_withoutParameters_shouldStopRefreshing() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();

    //when
    presenter.refreshWeather(parameters);

    // Then
    verify(view).stopRefreshing();
  }

  @Test
  public void restoreStateAndShowWeather_is_valid_shouldShowWeather() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, CUSTOM_QUERY);

    // Given a stubbed model
    givenAStubbedUIModel();

    //when
    presenter.restoreStateAndShowWeather(uiModel, THE_MODEL_IS_VALID);

    //then
    WeatherUI uiModel = presenter.getUiModel();
    verify(view).showWeather(uiModel);

  }

  @Test
  public void restoreStateAndShowWeather_isInvalid_shouldNotCallShowWeather() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, CUSTOM_QUERY);

    // Given a stubbed model
    givenAStubbedUIModel();

    //when
    presenter.restoreStateAndShowWeather(uiModel, THE_MODEL_IS_INVALID);

    //then
    assertEquals(null,presenter.getUiModel());
    verify(view, never()).showWeather(uiModel);
  }

  @Test
  public void transformModelToUiModel_shouldTransformDataProperly() {
    // Given a stubbed model
    givenAStubbedModel();

    // When
    WeatherUI localUiModel = presenter.transformModelToUiModel(model);

    // Then
    assertEquals(model.getCountry().getName(), localUiModel.getCountryLabel());
    assertEquals(model.getName(), localUiModel.getCityLabel());
    int icon = IconMapper.fromInt(model.getSkyDescription().get(0).getId()).getIcon();
    assertEquals(icon, localUiModel.getIcon());
    assertEquals(model.getSkyDescription().get(0).getDescription(), localUiModel.getSkyLabel());
    String temp = TemperatureFormatter.getTemperatureFormatted(model.getTemperatureInfo().getTemperature());
    assertEquals(temp, localUiModel.getTemperatureLabel());
    temp = TemperatureFormatter.getTemperatureFormatted(model.getTemperatureInfo().getMaximum());
    assertEquals(temp, localUiModel.getMaxTemperatureLabel());
    temp = TemperatureFormatter.getTemperatureFormatted(model.getTemperatureInfo().getMinimum());
    assertEquals(temp, localUiModel.getMinTemperatureLabel());

    }

  private void givenAStubbedModel() {
    List< SkyDescription > skyDescriptionList = new ArrayList< SkyDescription >();
    SkyDescription skyDescription = new SkyDescription(SKY_ICON_ID, "", SKY_DESCRIPTION);
    skyDescriptionList.add(skyDescription);
    TemperatureInfo temperatureInfo = new TemperatureInfo(TEMPERATURE, MINIMUM, MAXIMUM);
    CountryHolder country = new CountryHolder(COUNTRY);
    model = new WeatherInfo(CITY,  skyDescriptionList,  temperatureInfo,  country);
  }

  private void givenAStubbedUIModel() {
    givenAStubbedModel();
    uiModel = presenter.transformModelToUiModel(model);
  }

  @Test @Ignore
  public void onResponse_shouldExecuteHandler() {
    // Given a stubbed model
    givenAStubbedModel();

    //when
    presenter.onResponse(model);

    // Then: error: cannot mock databaseHandler
    //verify(databaseHandler).execute(any(Runnable.class));
  }

  @Test
  public void onError_shouldShowError() {
    //when
    presenter.onError(CUSTOM_ERROR);

    // Then
    verify(view).showError(CUSTOM_ERROR);
  }

  @Test @Ignore
  public void onOffline_shouldShowOffline() {
    //when
    presenter.onOffline();

    // Then error: cannot mock databaseHandler
    verify(view).showOffline();
    //verify(databaseHandler).execute(any(Runnable.class));
  }

  @Test @Ignore
  public void onDatabaseOperationFinished_whenThereIsWeatherInfo_shouldShowWeather() {
    //when
    presenter.onDatabaseOperationFinished();

    // Then error: cannot mock weatherInfoWrapper
    WeatherUI uiModel = presenter.getUiModel();
    verify(view).showWeather(uiModel);
  }

  @Test
  public void onDatabaseOperationFinished_whenThereIsNotWeatherInfo_shouldNotShowWeather() {
    //when
    presenter.onDatabaseOperationFinished();

    // Then
    verify(view, never()).showWeather(any(WeatherUI.class));
  }


  @Test
  public void onGeolocation_with_GPS_request_shouldCallIteractor() {
    // Given a a stubbed parameters with query lat, lon
    Map<String, String> parametersWithGPS = new HashMap<String, String>();
    parametersWithGPS.put(Constant.API_PARAMETER_LATITUDE, LAT);
    parametersWithGPS.put(Constant.API_PARAMETER_LONGITUDE, LON);

    //when
    presenter.onGeolocation(parametersWithGPS);

    // Then
    verify(searchWeatherInteractor).execute(anyMap(), weatherCallbackArgumentCaptor.capture());
  }

}
