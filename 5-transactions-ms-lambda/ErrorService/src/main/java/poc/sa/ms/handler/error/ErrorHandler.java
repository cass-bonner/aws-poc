package poc.sa.ms.handler.error;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import poc.sa.ms.client.error.ErrorServiceClient;
import poc.sa.ms.client.error.ErrorServiceClientImpl;
import poc.sa.ms.dao.error.ErrorWrapper;

public class ErrorHandler implements RequestStreamHandler, RequestHandler<Object, Object> {

  @Override
  public Object handleRequest(Object input, Context context) {
    return null;
  }

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
    LambdaLogger logger = context.getLogger();
    ErrorServiceClient errorServiceClient = new ErrorServiceClientImpl();
    try {
      ErrorWrapper errorWrapper = new Gson().fromJson(IOUtils.toString(inputStream, "UTF-8"), ErrorWrapper.class);

      // FulfillmentService fulfillmentService = new FulfillmentServiceImpl();
      // order = fulfillmentService.checkStockTake(order);

      logger.log("ErrorWrapper: " + errorWrapper);
      errorServiceClient.process(errorWrapper);

    } catch (Exception e) {
      logger.log("Exception thrown: " + e.getMessage());
    }
  }
}
