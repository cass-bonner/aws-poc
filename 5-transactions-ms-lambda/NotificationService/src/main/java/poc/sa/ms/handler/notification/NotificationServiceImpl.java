package poc.sa.ms.handler.notification;


import com.amazonaws.services.simpleemail.model.Destination;

import poc.sa.ms.common.model.order.OrderWorkflowState;

public class NotificationServiceImpl implements NotificationService {

  @Override
  public void notify(OrderWorkflowState orderWorkflowState, String order) {
    Destination destination = new Destination().withToAddresses(new String[]{});
    // park for now. seems to want access keys.
  }

}
