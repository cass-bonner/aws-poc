package poc.sa.ms.service.notification;

import poc.sa.ms.model.order.Order;
import poc.sa.ms.model.order.OrderWorkflowState;
import poc.sa.ms.service.user.UserService;
import poc.sa.ms.service.user.UserServiceImpl;

public class NotificationServiceImpl implements NotificationService {

  @Override
  public void notify(OrderWorkflowState orderWorkflowState, Order order) {
    UserService userService = new UserServiceImpl();
    // Call SES Here.
   String email = userService.getEmailAddress(order.getUsername());

  }

}
