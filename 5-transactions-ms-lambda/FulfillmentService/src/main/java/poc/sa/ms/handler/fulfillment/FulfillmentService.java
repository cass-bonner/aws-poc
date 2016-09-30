package poc.sa.ms.handler.fulfillment;

import java.util.List;

import poc.sa.ms.common.model.order.Order;

public interface FulfillmentService {
  
  /**
   * Confirm the availability and pricing for each of the items. If the price changed
   * or any items are not in stock, the customer is notified.
   * @param order
   * @return
   */
  public Order checkStockTake(Order order);
  /**
   * Based on availability and stock location relative to customer's address, 
   * determine shipping groups or return the one Order if all in one group
   * @param order
   * @return List<Order>
   */
  public List<Order> identifyShippingGroups(Order order);

}
