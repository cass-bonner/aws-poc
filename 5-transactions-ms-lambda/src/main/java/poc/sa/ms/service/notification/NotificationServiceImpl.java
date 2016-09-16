package poc.sa.ms.service.notification;


import com.amazonaws.services.simpleemail.model.Destination;

import poc.sa.ms.model.order.Order;
import poc.sa.ms.model.order.OrderWorkflowState;
import poc.sa.ms.service.error.ErrorService;

public class NotificationServiceImpl implements NotificationService {

  @Override
  public void notify(OrderWorkflowState orderWorkflowState, String order) {
    Destination destination = new Destination().withToAddresses(new String[]{ErrorService.EMAIL_OVERRIDE});
    // park for now. seems to want access keys.
  }

}
