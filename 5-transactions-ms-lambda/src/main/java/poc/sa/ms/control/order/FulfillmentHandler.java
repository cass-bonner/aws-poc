package poc.sa.ms.control.order;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import poc.sa.ms.model.order.Order;
import poc.sa.ms.service.fulfillment.FulfillmentService;
import poc.sa.ms.service.fulfillment.FulfillmentServiceImpl;


  public class FulfillmentHandler implements RequestStreamHandler, RequestHandler<Object, Object> {

    @Override
    public Object handleRequest(Object input, Context context) {
      return null;
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
      LambdaLogger logger = context.getLogger();
      try {
        Order order = new Gson().fromJson(IOUtils.toString(inputStream, "UTF-8"),
            Order.class);
        
        FulfillmentService fulfillmentService = new FulfillmentServiceImpl();
        order = fulfillmentService.checkStockTake(order);

        logger.log("Order: " + order);
      } catch (Exception e) {
        logger.log("Exception thrown: " + e.getMessage());
        e.printStackTrace();
      }
    }
}
