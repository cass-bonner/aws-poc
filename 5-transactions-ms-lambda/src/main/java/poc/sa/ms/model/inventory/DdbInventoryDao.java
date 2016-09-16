package poc.sa.ms.model.inventory;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import poc.sa.ms.exception.DaoException;

public class DdbInventoryDao implements InventoryDao {
  
  private static DdbInventoryDao instance = null;

  // credentials for the client come from the environment variables pre-configured by Lambda. These are tied to the
  // Lambda function execution role.
  private static AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient();

  /**
   * Returns the initialized default instance of the DdbInventoryDao
   *
   * @return An initialized DdbInventoryDao instance
   */
  public static DdbInventoryDao getInstance() {
      if (instance == null) {
          instance = new DdbInventoryDao();
      }

      return instance;
  }

  protected DdbInventoryDao() {
    // contain instantiation
  }

  /**
   * NB: This method would require a ConditionalWrite in practice. For demo we simplify
   * but 
   */
  @Override
  public StockStatus checkStockTake(String sku, int quantityRequested) {
    System.out.println("in checkStockTake dao");
    ItemStock itemStock = getItemById(sku);
    System.out.println("item stock: " + itemStock);
    if (itemStock == null) {
      return StockStatus.INVALID_SKU;
    } else if (itemStock.getQuantity() >= quantityRequested) {
      //Reduce the stock by the value requested.
      itemStock.setQuantity(itemStock.getQuantity() - quantityRequested);
      //TODO - change to conditionalwrite.
      System.out.println("saving itemstock after reducing quantity");
      getMapper().save(itemStock);
      return StockStatus.IN_STOCK;
    } else if (itemStock.getQuantity() < quantityRequested) {
      if (itemStock.isReOrdered()==true) {
        return StockStatus.ON_ORDER;
      } else {
        return StockStatus.OUT_OF_STOCK;
      }
    }
    return StockStatus.OUT_OF_STOCK; 
  }
  
  public ItemStock getItemById(String sku) throws DaoException {
    if (sku == null || sku.trim().equals("")) {
        throw new DaoException("Cannot lookup null or empty sku");
    }

    return getMapper().load(ItemStock.class, sku);
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
