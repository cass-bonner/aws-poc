package poc.sa.ms.service.error;

import com.google.gson.Gson;

import poc.sa.ms.model.error.DdbErrorDao;
import poc.sa.ms.model.error.ErrorDao;
import poc.sa.ms.model.error.ErrorWrapper;
import poc.sa.ms.model.order.Order;
import poc.sa.ms.model.order.OrderWorkflowState;
import poc.sa.ms.service.notification.NotificationService;
import poc.sa.ms.service.notification.NotificationServiceImpl;

public class ErrorServiceImpl implements ErrorService {
  
  
  
  ErrorDao errorDao = DdbErrorDao.getInstance();
  NotificationService notificationService = new NotificationServiceImpl();

  @Override
  public void process(ErrorWrapper errorWrapper) {
    errorDao.process(errorWrapper);
    notificationService.notify(OrderWorkflowState.ORDER_MODIFICATION, errorWrapper.getOrder());
    
  }

  @Override
  public void process(Order order, String serviceId, String messageDetail) {
    ErrorWrapper errorWrapper = new ErrorWrapper();
    errorWrapper.setOrder(new Gson().toJson(order));
    errorWrapper.setMessageDetail(messageDetail);
    errorWrapper.setServiceId(serviceId);
    process(errorWrapper);
    
  }

}
