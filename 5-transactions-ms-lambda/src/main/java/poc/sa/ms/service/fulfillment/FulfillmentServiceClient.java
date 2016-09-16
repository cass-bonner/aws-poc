package poc.sa.ms.service.fulfillment;

import poc.sa.ms.model.order.Order;

public interface FulfillmentServiceClient {
  public void fulfill(Order order);
}
