package poc.sa.ms.handler.order;

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

import poc.sa.ms.client.payment.PaymentClientService;
import poc.sa.ms.client.payment.PaymentClientServiceImpl;
import poc.sa.ms.common.model.exception.DaoException;
import poc.sa.ms.common.model.exception.ExceptionMessage;
import poc.sa.ms.common.model.exception.InvalidRequestException;
import poc.sa.ms.common.model.order.Order;
import poc.sa.ms.dao.order.DaoFactory;
import poc.sa.ms.dao.order.OrderDao;
import poc.sa.ms.model.order.CreateOrderResponse;


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

    String body = IOUtils.toString(inputStream);
    
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

    outputStream.write(orderId.getBytes());
  }


  protected void authorize(Order createOrderRequest) {
    PaymentClientService paymentClientService = new PaymentClientServiceImpl();
    
    System.out.println("order: " + createOrderRequest);    
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
