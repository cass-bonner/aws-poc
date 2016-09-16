package poc.sa.ms.service.fulfillment;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import poc.sa.ms.model.inventory.DdbInventoryDao;
import poc.sa.ms.model.inventory.InventoryDao;
import poc.sa.ms.model.inventory.StockStatus;
import poc.sa.ms.model.order.Item;
import poc.sa.ms.model.order.Order;
import poc.sa.ms.service.error.ErrorService;
import poc.sa.ms.service.error.ErrorServiceImpl;

public class FulfillmentServiceImpl implements FulfillmentService {
  private static Logger logger = Logger.getLogger(FulfillmentServiceImpl.class);

  @Override
  public Order checkStockTake(Order order) {
    InventoryDao inventoryDao = DdbInventoryDao.getInstance();
    ErrorService errorService = new ErrorServiceImpl();
    
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
            errorService.process(order,"FulfillmentService","Removed item from order as it was an invalid sku: " + item.getSku() + 
                " for order: " + order.getOrderId() + ". Remaining items will be shipped.");
          } else {
            errorService.process(order,"FulfillmentService","Removed item from order as it was an invalid sku: " + item.getSku() + 
                " for order: " + order.getOrderId() + ". No remaining items in order.");
           }
          break;
        case ON_ORDER: 
          errorService.process(order,"FulfillmentService"," Item: " + item.getSku() + 
              " for order: " + order.getOrderId() + " is being ordered but there may be a small delay.");
          break;
        case OUT_OF_STOCK: 
          
          iterator.remove();
          
          if (order.getItems().size()>0) {
            errorService.process(order,"FulfillmentService","Removed item from order as it was out of stock: " + item.getSku() + 
                " for order: " + order.getOrderId() + ". Remaining items will be shipped.");
          } else {
            errorService.process(order,"FulfillmentService","Removed item from order as it was out of stock: " + item.getSku() + 
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
