package poc.sa.ms.service.notification;

import poc.sa.ms.model.order.Order;
import poc.sa.ms.model.order.OrderWorkflowState;

public interface NotificationService {
  public void notify(OrderWorkflowState orderWorkflowState, String order);

}
