package poc.sa.ms.client.payment;

import poc.sa.ms.common.model.order.payment.CreditCardPaymentDetail;

/**
 * Service API providing method invocations that will invoke
 * the relevant methods in Payment microservice
 * 
 * @author bonnerca
 *
 */

public interface PaymentClientService {
  /**
   * Verify whether the PaymentDetails provided are valid. 
   * @param creditCardPaymentPaymentDetail
   */
  public void verify(CreditCardPaymentDetail creditCardPaymentPaymentDetail);
  /**
   * Pre-authorize the payment as required and where possible
   * @param creditCardPaymentPaymentDetail
   */
  public void preAuth(CreditCardPaymentDetail creditCardPaymentPaymentDetail);
  

}
