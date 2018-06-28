package training.globant.myweather.data.net;

import java.io.IOException;
import java.lang.annotation.Annotation;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import training.globant.myweather.data.model.ErrorInfo;

/**
 * Represents a Error Helper for a rest API service
 *
 * @author Francisco Llaryora
 * @version 1.0
 * @since 1.0
 */

public class ErrorHelper {

  /**
   * Parses a response from one type to other type
   *
   * @param response message with wrong data type
   * @return error data model instance
   */
  static public ErrorInfo parseError(Response<?> response, Retrofit retrofit) {
    ErrorInfo errorResponse = new ErrorInfo();
    if (retrofit != null) {
      Converter<ResponseBody, ErrorInfo> converter =
          retrofit.responseBodyConverter(ErrorInfo.class, new Annotation[0]);
      try {
        if (response.errorBody() != null) {
          errorResponse = converter.convert(response.errorBody());
        }
      } catch (IOException ignore) {
      }
    }
    return errorResponse;
  }
}
