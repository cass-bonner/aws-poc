package poc.sa.ms.model.order;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class DdbCatalogItemDao {
  

  
  public void createCatalogItem(CatalogItem catalogItem) {
    AmazonDynamoDBClient client = new AmazonDynamoDBClient(new ProfileCredentialsProvider("ob-admin"));
    DynamoDBMapper mapper = new DynamoDBMapper(client);
    client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
    
    mapper.save(catalogItem);  
  }

}
