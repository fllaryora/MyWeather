package training.globant.myweather.domain;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import org.junit.Before;
import org.junit.Test;
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
import training.globant.myweather.data.model.CountryHolder;
import training.globant.myweather.data.model.ErrorInfo;
import training.globant.myweather.data.model.SkyDescription;
import training.globant.myweather.data.model.TemperatureInfo;
import training.globant.myweather.data.model.WeatherInfo;
import training.globant.myweather.data.net.NoConnectivityException;
import training.globant.myweather.data.net.WeatherAPIClient;
import training.globant.myweather.data.utils.Constant;


public class SearchWeatherInteractorTest {

  private static final String COUNTRY = "AR";
  private static final double TEMPERATURE = 15.5;
  private static final double MINIMUM = 5.5;
  private static final double MAXIMUM = 25.5;
  private static final String SKY_DESCRIPTION = "clear";
  private static final int SKY_ICON_ID = 800;
  private static final String CITY = "Córdoba";
  private static final String CUSTOM_QUERY = "Córdoba,AR";

  private static WeatherInfo model;

  @Mock
  private WeatherAPIClient.OpenWeatherMap weatherClient;


  @InjectMocks
  private SearchWeatherInteractor searchWeatherInteractor;

  @Before
  public void setUp() throws Exception{
    // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
    // inject the mocks in the test the initMocks method needs to be called.
    MockitoAnnotations.initMocks(this);
  }


  @Test
  public void execute_shouldCallOnResponse() {
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
    //Given Call
    final Call<WeatherInfo> mockedCall = Mockito.mock(Call.class);
    WeatherCallback callback = Mockito.mock(WeatherCallback.class);
    // When
    Mockito.doAnswer(new Answer() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        Callback<WeatherInfo> callback = invocation.getArgumentAt(0, Callback.class);

        callback.onResponse(mockedCall, Response.success(model));

        return null;
      }
    }).when(mockedCall).enqueue(any(Callback.class));

    when(weatherClient.searchWeatherByOptions(parameters)).thenReturn(mockedCall);

    searchWeatherInteractor.execute(parameters,callback);

    // Then
    verify(callback).onResponse(model);
  }

  @Test
  public void execute_shouldCallOnOffline() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, CUSTOM_QUERY);

    //Given Call
    final Call<WeatherInfo> mockedCall = Mockito.mock(Call.class);
    WeatherCallback callback = Mockito.mock(WeatherCallback.class);
    // When
    Mockito.doAnswer(new Answer() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        Callback<WeatherInfo> callback = invocation.getArgumentAt(0, Callback.class);

        callback.onFailure(mockedCall, new NoConnectivityException());

        return null;
      }
    }).when(mockedCall).enqueue(any(Callback.class));

    when(weatherClient.searchWeatherByOptions(parameters)).thenReturn(mockedCall);

    searchWeatherInteractor.execute(parameters,callback);

    // Then
    verify(callback).onOffline();
  }

  @Test
  public void execute_shouldCallOnError() {
    // Given a a stubbed parameters with query q
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(Constant.API_PARAMETER_QUERY, CUSTOM_QUERY);
    final ErrorInfo error = new ErrorInfo("Error",787);

    //Given Call
    final Call<WeatherInfo> mockedCall = Mockito.mock(Call.class);
    WeatherCallback callback = Mockito.mock(WeatherCallback.class);
    // When

    Mockito.doAnswer(new Answer() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        Callback<WeatherInfo> callback = invocation.getArgumentAt(0, Callback.class);

        String json = new Gson().toJson(error);
        callback.onResponse(mockedCall, Response.<WeatherInfo>error(404,ResponseBody.create(MediaType.parse("application/json") ,json)));
        return null;
      }
    }).when(mockedCall).enqueue(any(Callback.class));

    when(weatherClient.searchWeatherByOptions(parameters)).thenReturn(mockedCall);
    searchWeatherInteractor.execute(parameters,callback);

    // Then
    verify(callback).onError(anyString());
  }

}
