package poc.sa.ms.client.error;
import poc.sa.ms.common.model.order.Order;
import poc.sa.ms.dao.error.ErrorWrapper;

public interface ErrorServiceClient {
  public static String EMAIL_OVERRIDE="bonnerca";

  void process(ErrorWrapper errorWrapper);

  void process(Order order, String service, String messageDetail);

}