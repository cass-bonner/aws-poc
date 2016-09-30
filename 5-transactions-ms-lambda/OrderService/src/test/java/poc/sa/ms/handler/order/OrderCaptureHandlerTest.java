package poc.sa.ms.handler.order;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import poc.sa.ms.common.model.order.Item;
import poc.sa.ms.common.model.order.Order;
import poc.sa.ms.common.model.order.payment.CreditCardPaymentDetail;
import poc.sa.ms.common.model.order.payment.CreditCardProvider;
import poc.sa.ms.handler.order.OrderCaptureHandler;

public class OrderCaptureHandlerTest {

  private static final Logger logger = Logger.getLogger(OrderCaptureHandlerTest.class);

  /**
   * Tests that valid CreateOrderRequests json can be unmarshalled
   * 
   * @throws IOException
   */
  @Test
  public void testHandler2() throws IOException {

    String json = generateSampleJson();
    System.out.println("json: " + json);
    InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    OrderCaptureHandler orderCaptureHandler = new OrderCaptureHandler();
    orderCaptureHandler.handleRequest(stream, outputStream, createContext());
    
    System.out.println("Order id:::::: " + new String(outputStream.toByteArray()));

  }

  /**
   * Utility to generate valid json { "username": "user1", "createDate": "Sep
   * 11, 2016 8:17:44 AM", "items": [ { "lineItemId": "23455FB",
   * "lineItemDescription": "Blue Polo Shirt", "lineItemType": "Clothing",
   * "supplierName": "Crew", "quantity": 1 }, { "lineItemId": "23455R",
   * "lineItemDescription": "Red Polo Shirt", "lineItemType": "Clothing",
   * "supplierName": "Crew", "quantity": 1 } ], "paymentDetail": {
   * "netPaymentAmount": 47.22999999999999687361196265555918216705322265625,
   * "creditCardProvider": "VISA", "creditCardNumber": 4.123456789101112E15,
   * "expiryDate": "Jul 11, 2019 12:00:00 AM", "verificationCode": "397" } }
   * 
   * @return
   */
  // @Test
  public static String generateSampleJson() {
    
   

    Item item = new Item("23455FB", "Blue Polo Shirt", "Clothing", "Crew", 1);
    Item item2 = new Item("23455R", "Red Polo Shirt", "Clothing", "Crew", 1);
    List<Item> items = new ArrayList<Item>();
    items.add(item);
    items.add(item2);

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, 2019);
    cal.set(Calendar.MONTH, 06);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    Date d = cal.getTime();

    CreditCardPaymentDetail paymentDetail = new CreditCardPaymentDetail(CreditCardProvider.VISA.toString(),
        "4123456789101112", d, "397", new BigDecimal(47.23));

    Order order = new Order("user1", items, paymentDetail);
    String json = getGson().toJson(order);
    json = json.replaceAll("paymentDetail", "creditCardPaymentDetail");
    System.out.println(json);
    Order orderN = getGson().fromJson(json, Order.class);
    System.out.println(orderN);
    
    String stream = "{ \"action\": \"poc.sa.ms.handler.order.fulfillment.OrderCaptureHandler\", \"body\": { \"username\": \"user1\","
        + " \"createDate\": \"Sep 11, 2016 8:17:44 AM\", \"items\": [ { \"lineItemId\": \"23455FB\", \"lineItemDescription\": "
        + "\"Blue Polo Shirt\", \"lineItemType\": \"Clothing\", \"supplierName\": \"Crew\", \"quantity\": 1 }, { \"lineItemId\": "
        + "\"23455R\", \"lineItemDescription\": \"Red Polo Shirt\", \"lineItemType\": \"Clothing\", \"supplierName\": \"Crew\", "
        + "\"quantity\": 1 } ], \"creditCardPaymentDetail\": { \"netPaymentAmount\": 47.23, \"creditCardProvider\": \"VISA\", "
        + "\"creditCardNumber\": 4123456789101112, \"expiryDate\": \"Jul 11, 2019 12:00:00 AM\", \"verificationCode\": \"397\" } } }";
    return stream;

  }
  
 // @Test
  public void generateSampleJson2() {

    Item item = new Item("23455FB", "Blue Polo Shirt", "Clothing", "Crew", 1);
    Item item2 = new Item("23455R", "Red Polo Shirt", "Clothing", "Crew", 1);
    List<Item> items = new ArrayList<Item>();
    items.add(item);
    items.add(item2);

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, 2019);
    cal.set(Calendar.MONTH, 06);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    Date d = cal.getTime();

    CreditCardPaymentDetail paymentDetail = new CreditCardPaymentDetail(CreditCardProvider.VISA.toString(),
        "4123456789101112", d, "397", new BigDecimal(47.23));

    Order order = new Order("user1", items, paymentDetail);
    String json = getGson().toJson(order);
    json = json.replaceAll("paymentDetail", "creditCardPaymentDetail");
    System.out.println(json);
    Order orderN = getGson().fromJson(json, Order.class);
    System.out.println(orderN);

  }

  protected static Gson getGson() {
    return new GsonBuilder().setPrettyPrinting().create();
  }

  private Context createContext() {
    TestLambdaContext ctx = new TestLambdaContext();

    ctx.setFunctionName("OrderCaptureHandler");

    return ctx;
  }
  


}
