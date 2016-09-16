package poc.sa.ms.service.error;
import poc.sa.ms.model.error.ErrorWrapper;
import poc.sa.ms.model.order.Order;

public interface ErrorService {
  public static String EMAIL_OVERRIDE="bonnerca";

  void process(ErrorWrapper errorWrapper);

  void process(Order order, String service, String messageDetail);

}