package poc.sa.ms.service.error.client;

import poc.sa.ms.model.order.Order;

public interface ErrorServiceClient {
  public void process(Order order,String serviceId, String messageDetail);
}
