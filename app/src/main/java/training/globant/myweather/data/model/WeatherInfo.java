package training.globant.myweather.data.model;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by francisco on 01/05/18.
 */
//https://gist.github.com/karlstolley/99b733313a9a13782843
//https://openweathermap.org/current
public class WeatherInfo {
    //"cod":200, -- Internal parameter
    @SerializedName("cod")
    private int cod;
    //example "id":3860259,
    @SerializedName("id")
    private int id;
    //example "name":"Córdoba",
    @SerializedName("name")
    private String name;
    // "coord":{ ... }
    @SerializedName("coord")
    private Coordinates coordinates;
    //"dt":1525204800,  Time of data calculation, unix, UTC
    @SerializedName("dt")
    private long dateTime;
    //"weather":[ ... ]
    @SerializedName("weather")
    private List<MetaInfo> metaInfo ;
    //"base":"stations", -- Internal parameter
    @SerializedName("base")
    private String base;
    //"main":{ ...}
    @SerializedName("main")
    private TemperatureInfo temperatureInfo;
    //"visibility":2000, meter
    @SerializedName("visibility")
    private int visibility;
    //"wind":{ ... }
    @SerializedName("wind")
    private WindInfo wind;
    //"sys":{ ... }
    @SerializedName("sys")
    private SysInfo sys;
    //"clouds":{ ..}
    @SerializedName("clouds")
    private CloudInfo cloudInfo ;
    @SerializedName("rain")
    private PrecipitationInfo rainInfo;
    @SerializedName("snow")
    private PrecipitationInfo snowInfo;

    public int getCod() {
        return cod;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public long getDateTime() {
        return dateTime;
    }

    public List<MetaInfo> getMetaInfo() {
        return metaInfo;
    }

    public String getBase() {
        return base;
    }

    public TemperatureInfo getTemperatureInfo() {
        return temperatureInfo;
    }

    public int getVisibility() {
        return visibility;
    }

    public WindInfo getWind() {
        return wind;
    }

    public SysInfo getSys() {
        return sys;
    }

    public CloudInfo getCloudInfo() {
        return cloudInfo;
    }

    public PrecipitationInfo getRainInfo() {
        return rainInfo;
    }

    public PrecipitationInfo getSnowInfo() {
        return snowInfo;
    }

    public WeatherInfo(int cod, int id, String name, Coordinates coordinates, long dateTime,
                       List<MetaInfo> metaInfo, String base, TemperatureInfo temperatureInfo,
                       int visibility, WindInfo wind, SysInfo sys, CloudInfo cloudInfo,
                       PrecipitationInfo rainInfo, PrecipitationInfo snowInfo) {
        this.cod = cod;
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.dateTime = dateTime;
        this.metaInfo = metaInfo;
        this.base = base;
        this.temperatureInfo = temperatureInfo;
        this.visibility = visibility;
        this.wind = wind;
        this.sys = sys;
        this.cloudInfo = cloudInfo;
        this.rainInfo = rainInfo;
        this.snowInfo = snowInfo;
    }

    public WeatherInfo() {
        this.cod = 0;
        this.id = 0;
        this.name = "";
        this.coordinates = null;
        this.dateTime = 0;
        this.metaInfo = null;
        this.base = "";
        this.temperatureInfo = null;
        this.visibility = 0;
        this.wind = null;
        this.sys = null;
        this.cloudInfo = null;
        this.rainInfo = null;
        this.snowInfo = null;
    }
}
