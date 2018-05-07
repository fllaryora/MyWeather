package training.globant.myweather.data.net;

import java.io.IOException;
import java.lang.annotation.Annotation;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import training.globant.myweather.data.model.ErrorInfo;

/**
 * Created by francisco on 01/05/18.
 */

public class ErrorHelper {

  static public ErrorInfo parseError(Response<?> response) {
    Retrofit retrofit = WeatherAPIClient.provideRestClient();
    Converter<ResponseBody, ErrorInfo> converter =
        retrofit.responseBodyConverter(ErrorInfo.class, new Annotation[0]);

    ErrorInfo errorResponse;
    try {
      if (response.errorBody() != null) {
        errorResponse = converter.convert(response.errorBody());
      } else {
        errorResponse = new ErrorInfo();
      }
    } catch (IOException e) {
      return new ErrorInfo();
    }
    return errorResponse;
  }
}
