package training.globant.myweather.presentation.show_weather.presenter;

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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import training.globant.myweather.data.WeatherCallback;
import training.globant.myweather.data.database.DatabaseHandler;
import training.globant.myweather.data.model.CountryHolder;
import training.globant.myweather.data.model.SkyDescription;
import training.globant.myweather.data.model.TemperatureInfo;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.net.WeatherAPIClient;
import training.globant.myweather.data.utils.Constant;
import training.globant.myweather.domain.SearchWeatherInteractor;
import training.globant.myweather.presentation.show_weather.ShowWeatherContract;
import training.globant.myweather.presentation.show_weather.model.IconMapper;
import training.globant.myweather.presentation.show_weather.model.TemperatureFormatter;
import training.globant.myweather.presentation.show_weather.model.WeatherUI;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
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

  /**
   * This mocks are private members of searchWeatherInteractor
   */
  @Mock
  private WeatherAPIClient.OpenWeatherMap weatherClient;
  /**
   * This mocks are private members of presenter
   */
  @Mock
  private ShowWeatherContract.View view;

  @InjectMocks
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
  public void loadWeather_shouldShowWeather() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, CUSTOM_QUERY);

    // Given a stubbed model
    List< SkyDescription > skyDescriptionList = new ArrayList< SkyDescription >();
    SkyDescription skyDescription = new SkyDescription(SKY_ICON_ID, "", SKY_DESCRIPTION);
    skyDescriptionList.add(skyDescription);
    TemperatureInfo temperatureInfo = new TemperatureInfo(TEMPERATURE, MINIMUM, MAXIMUM);
    CountryHolder country = new CountryHolder(COUNTRY);
    model = new WeatherInfo(CITY,  skyDescriptionList,  temperatureInfo,  country);


    presenter.loadWeather(parameters);
    // Callback is captured and invoked
    //TODO execute IS NOT CAPTURED and the call is made
    verify(searchWeatherInteractor).execute(parameters, weatherCallbackArgumentCaptor.capture());
    weatherCallbackArgumentCaptor.getValue().onResponse(model);

    // Then
    WeatherUI uiModel = presenter.getUiModel();
    verify(view).showWeather(uiModel);
  }

  @Test
  public void transformModelToUiModel_shouldTransformDataProperly() {
    // Given a stubbed model
    List< SkyDescription > skyDescriptionList = new ArrayList< SkyDescription >();
    SkyDescription skyDescription = new SkyDescription(SKY_ICON_ID, "", SKY_DESCRIPTION);
    skyDescriptionList.add(skyDescription);
    TemperatureInfo temperatureInfo = new TemperatureInfo(TEMPERATURE, MINIMUM, MAXIMUM);
    CountryHolder country = new CountryHolder(COUNTRY);
    model = new WeatherInfo(CITY,  skyDescriptionList,  temperatureInfo,  country);

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

}
