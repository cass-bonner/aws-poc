package poc.sa.ms.handler.notification;

import poc.sa.ms.common.model.order.OrderWorkflowState;

public interface NotificationService {
  public void notify(OrderWorkflowState orderWorkflowState, String order);

}
