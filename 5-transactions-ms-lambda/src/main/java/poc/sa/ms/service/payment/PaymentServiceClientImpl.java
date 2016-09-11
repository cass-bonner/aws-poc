package poc.sa.ms.service.payment;

public class PaymentServiceClientImpl implements PaymentServiceClient {

  @Override
  public void verifyPost(String orderPaymentDetail) {
    System.out.println("should have ivoked api gateway");

  }

}
