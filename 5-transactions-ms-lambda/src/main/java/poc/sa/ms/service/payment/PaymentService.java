package poc.sa.ms.service.payment;

import poc.sa.ms.model.order.payment.CreditCardPaymentDetail;

/**
 * Service API providing method invocations that will invoke
 * the relevant methods in Payment microservice
 * 
 * @author bonnerca
 *
 */

public interface PaymentService {
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