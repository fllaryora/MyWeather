package training.globant.myweather.presentation.show_weather.model;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import training.globant.myweather.data.model.CountryHolder;
import training.globant.myweather.data.model.SkyDescription;
import training.globant.myweather.data.model.TemperatureInfo;
import training.globant.myweather.data.model.WeatherInfo;

/**
 * Unit tests for {@link WeatherUI} model.
 *
 * @author Francisco Llaryora
 */
public class WeatherUITest {
  private static final String COUNTRY = "AR";
  private static final double TEMPERATURE = 15.5;
  private static final double MINIMUM = 5.5;
  private static final double MAXIMUM = 25.5;
  private static final String SKY_DESCRIPTION = "clear";
  private static final int SKY_ICON_ID = 800;
  private static final String CITY = "Córdoba";

  private static WeatherInfo model;
  @Before
  public void setUp() {
    // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
    // inject the mocks in the test the initMocks method needs to be called.
    MockitoAnnotations.initMocks(this);


    List< SkyDescription > skyDescriptionList = new ArrayList< SkyDescription >();
    SkyDescription skyDescription = new SkyDescription(SKY_ICON_ID, "", SKY_DESCRIPTION);
    skyDescriptionList.add(skyDescription);
    TemperatureInfo temperatureInfo = new TemperatureInfo(TEMPERATURE, MINIMUM, MAXIMUM);
    CountryHolder country = new CountryHolder(COUNTRY);
    model = new WeatherInfo(CITY,  skyDescriptionList,  temperatureInfo,  country);

  }
  @Test
  public void transformModelToUiModel_shouldTransformDataProperly() {

    // Then
    assertThat(model.getCountry().getName(), instanceOf(String.class));
    assertThat(model.getName(), instanceOf(String.class));
    assertThat(model.getSkyDescription(), instanceOf(List.class));
    assertThat(model.getSkyDescription().get(0), instanceOf(SkyDescription.class));
    assertThat(model.getSkyDescription().get(0).getId(), instanceOf(Integer.class));
    assertThat(model.getSkyDescription().get(0).getDescription(), instanceOf(String.class));
    assertThat(model.getSkyDescription().get(0).getName(), instanceOf(String.class));
    assertThat(model.getTemperatureInfo(), instanceOf(TemperatureInfo.class));
    assertThat(model.getTemperatureInfo().getTemperature(), instanceOf(Double.class));
    assertThat(model.getTemperatureInfo().getMaximum(), instanceOf(Double.class));
    assertThat(model.getTemperatureInfo().getMinimum(), instanceOf(Double.class));

  }

}
