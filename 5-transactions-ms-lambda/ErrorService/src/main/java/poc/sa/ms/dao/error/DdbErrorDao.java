package poc.sa.ms.dao.error;

import org.apache.log4j.Logger;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class DdbErrorDao implements ErrorDao {
  
  private static DdbErrorDao instance = null;
  private static Logger logger = Logger.getLogger(DdbErrorDao.class);

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
  public static DdbErrorDao getInstance() {
    if (instance == null) {
      instance = new DdbErrorDao();
    }
    return instance;
  }

  protected DdbErrorDao() {
    // protect instantiation
  }

  @Override
  public void process(ErrorWrapper errorWrapper) {
    getMapper().save(errorWrapper);

  }
  
  /**
   * Returns a DynamoDBMapper object initialized with the default DynamoDB
   * client
   *
   * @return An initialized DynamoDBMapper
   */
  protected DynamoDBMapper getMapper() {
    ddbClient.setRegion(Regions.getCurrentRegion());
    return new DynamoDBMapper(ddbClient);
  }

}
