package poc.sa.ms.model.order;

import java.util.List;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.google.gson.Gson;

import poc.sa.ms.exception.DaoException;

public class DdbOrderDao implements OrderDao {

  private static DdbOrderDao instance = null;

  // credentials for the client come from the environment variables
  // pre-configured by Lambda. These are tied to the
  // Lambda function execution role.
  private static AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient();

  /**
   * Returns the initialized default instance of the OrderDao
   * 
   * Not threadsafe but ok since contained instantiation
   *
   * @return An initialized OrderDao instance
   */
  public static DdbOrderDao getInstance() {
    if (instance == null) {
      instance = new DdbOrderDao();
    }
    return instance;
  }

  protected DdbOrderDao() {
    // protect instantiation
  }

  @Override
  public String createOrder(Order order) throws DaoException {

    validate(order);
    
    PersistedOrder persistedOrder = new PersistedOrder(new Gson().toJson(order));

    getMapper().save(persistedOrder);

    return persistedOrder.getOrderId();
  }

  /**
   * Local validation logic before saving otherwise if invalid throw exception
   * 
   * @param order
   */
  protected void validate(Order order) {
    if (order.getUsername() == null || order.getUsername().trim().equals("") || order.getItems() == null
        || order.getItems().size() <= 0 || order.getCreditCardPaymentDetail() == null
        || order.getCreditCardPaymentDetail().getPaymentType() == null || order.getCreditCardPaymentDetail().getNetPaymentAmount() == null

    ) {
      throw new DaoException("Cannot persist order without username, items, and payment");
    }

  }

  @Override
  public Order getOrderById(String orderId) throws DaoException {
    if (orderId == null || orderId.trim().equals("")) {
      throw new DaoException("Cannot lookup null or empty orderId");
    }

    return getMapper().load(Order.class, orderId);
  }

  /**
   * TODO: need different function here and local or global secondary index on
   * username
   */
  @Override
  public List<Order> getOrders(String username) throws DaoException {
    if (username == null || username.trim().equals("")) {
      throw new DaoException("Cannot lookup null or empty username");
    }

    // this won't work change to index lookup.
    // return getMapper().load(List<Order.class>, orderId);
    return null;
  }

  /**
   * Returns a DynamoDBMapper object initialized with the default DynamoDB
   * client
   *
   * @return An initialized DynamoDBMapper
   */
  protected DynamoDBMapper getMapper() {
    ddbClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
    return new DynamoDBMapper(ddbClient);
  }

}
