package poc.sa.ms.control.order;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import poc.sa.ms.exception.DaoException;
import poc.sa.ms.exception.ExceptionMessage;
import poc.sa.ms.exception.InvalidRequestException;
import poc.sa.ms.model.DaoFactory;
import poc.sa.ms.model.action.CreateOrderRequest;
import poc.sa.ms.model.action.CreateOrderResponse;
import poc.sa.ms.model.order.Order;
import poc.sa.ms.model.order.OrderDao;
import poc.sa.ms.service.payment.PaymentClientService;
import poc.sa.ms.service.payment.PaymentClientServiceImpl;


public class OrderCaptureHandler implements RequestStreamHandler, RequestHandler<Object, Object> {
  private LambdaLogger logger;
  private static Logger log = Logger.getLogger(OrderCaptureHandler.class);

  @Override
  public Object handleRequest(Object input, Context context) {
      return null;
  }

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context lambdaContext) throws IOException {


    logger = lambdaContext.getLogger();
    JsonParser parser = new JsonParser();
    JsonObject inputObj;
    try {
        inputObj = parser.parse(IOUtils.toString(inputStream)).getAsJsonObject();
    } catch (IOException e) {
        logger.log("ErrorWrapper while reading request\n" + e.getMessage());
        throw new RuntimeException(e.getMessage());
    }
    // handy if want to utilize request dispatcher pattern. here we keep it simple
    log.debug("inputObj: " + inputObj);
    logger.log("Action: " + inputObj.get("action").getAsString());
    
    JsonObject body = null;
    if (inputObj.get("body") != null) {
        body = inputObj.get("body").getAsJsonObject();
    }
    logger.log("Stream body: " +body);

    
    Order order = getGson().fromJson(body, Order.class);

    logger.log("order:: " + order);
    validateOrderRequest(order);
    
    System.out.println("order validated: " + order);
    
    //authorize payment if > 100 AUD
    authorize(order);
    
    log.debug("order validated: " + order);
    

    OrderDao dao = DaoFactory.getOrderDao();

    String orderId;

    try {
      orderId = dao.createOrder(order);
    } catch (final DaoException e) {
      logger.log("ErrorWrapper while creating new order\n" + e.getMessage());
      throw new RuntimeException(ExceptionMessage.EX_DAO_ERROR);
    }

    if (orderId == null || orderId.trim().equals("")) {
      logger.log("orderId is null or empty");
      throw new RuntimeException(ExceptionMessage.EX_DAO_ERROR);
    }
    

    CreateOrderResponse output = new CreateOrderResponse();
    output.setOrderId(orderId);
    System.out.println("*******************orderId: " + orderId);

  //  return getGson().toJson(output);
    outputStream.write(orderId.getBytes());
  }

  protected CreateOrderRequest toJson(JsonObject request, Class<CreateOrderRequest> classz) {
    return getGson().fromJson(request, classz);
  }

  protected void authorize(Order createOrderRequest) {
    PaymentClientService paymentClientService = new PaymentClientServiceImpl();
    
    System.out.println("order: " + createOrderRequest);
    paymentClientService.verify(createOrderRequest.getCreditCardPaymentDetail());
    
    paymentClientService.preAuth(createOrderRequest.getCreditCardPaymentDetail());    
  }

  protected void validateOrderRequest(Order order) {
    
    System.out.println("order: " + order);
    
    if (order.getUsername() == null || order.getUsername().trim().equals("") || order.getItems() == null
        || order.getItems().size() <= 0 || order.getCreditCardPaymentDetail() == null
        || order.getCreditCardPaymentDetail().getPaymentType() == null || order.getCreditCardPaymentDetail().getNetPaymentAmount() == null

    ) {
      throw new InvalidRequestException(ExceptionMessage.INVALID_PAYLOAD);
    }
    
  }

  protected Gson getGson() {
    return new GsonBuilder().setPrettyPrinting().create();
  }

  

  

}
