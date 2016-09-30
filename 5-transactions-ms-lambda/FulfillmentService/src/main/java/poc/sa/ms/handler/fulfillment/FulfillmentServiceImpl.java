package poc.sa.ms.handler.fulfillment;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import poc.sa.ms.client.error.ErrorServiceClient;
import poc.sa.ms.client.error.ErrorServiceClientImpl;
import poc.sa.ms.common.model.order.Item;
import poc.sa.ms.common.model.order.Order;
import poc.sa.ms.dao.fulfillment.DdbInventoryDao;
import poc.sa.ms.dao.fulfillment.InventoryDao;
import poc.sa.ms.dao.fulfillment.StockStatus;

public class FulfillmentServiceImpl implements FulfillmentService {
  private static Logger logger = Logger.getLogger(FulfillmentServiceImpl.class);

  @Override
  public Order checkStockTake(Order order) {
    InventoryDao inventoryDao = DdbInventoryDao.getInstance();
    ErrorServiceClient errorServiceClient = new ErrorServiceClientImpl();
    
    System.out.println("in checkStockTake: " + order.getItems().size());
    for (Iterator<Item> iterator = order.getItems().iterator(); iterator.hasNext();) {
      Item item = iterator.next();
      System.out.println("item: " + item);
      int i = 0;
      StockStatus stockStatus= inventoryDao.checkStockTake(item.getSku(), item.getQuantity());
      System.out.println("stockStatus: " + stockStatus);
      switch (stockStatus) {
        case IN_STOCK: 
          // all good  
          break;
        case INVALID_SKU:
          // remove item from order 
          // notify customer.
          // not the best way to do this but will work for the poc

          iterator.remove();

          if (order.getItems().size()>0) {
            errorServiceClient.process(order,"FulfillmentService","Removed item from order as it was an invalid sku: " + item.getSku() + 
                " for order: " + order.getOrderId() + ". Remaining items will be shipped.");
          } else {
            errorServiceClient.process(order,"FulfillmentService","Removed item from order as it was an invalid sku: " + item.getSku() + 
                " for order: " + order.getOrderId() + ". No remaining items in order.");
           }
          break;
        case ON_ORDER: 
          errorServiceClient.process(order,"FulfillmentService"," Item: " + item.getSku() + 
              " for order: " + order.getOrderId() + " is being ordered but there may be a small delay.");
          break;
        case OUT_OF_STOCK: 
          
          iterator.remove();
          
          if (order.getItems().size()>0) {
            errorServiceClient.process(order,"FulfillmentService","Removed item from order as it was out of stock: " + item.getSku() + 
                " for order: " + order.getOrderId() + ". Remaining items will be shipped.");
          } else {
            errorServiceClient.process(order,"FulfillmentService","Removed item from order as it was out of stock: " + item.getSku() + 
                " for order: " + order.getOrderId() + ". No remaining items in order.");
          }
          break;
      
      }
      System.out.println("after switch");
      
      i++;
    }
    System.out.println("returning order: " + order);
    return order;
  }

  @Override
  public List<Order> identifyShippingGroups(Order order) {
    // TODO Auto-generated method stub
    return null;
  }

}
