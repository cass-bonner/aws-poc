package poc.sa.ms.control.order.payment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import poc.sa.ms.model.order.payment.CreditCardPaymentDetail;

/**
 * Lamda function that pre authorises the payment if required.
 * 
 * @see poc.sa.ms.service.payment.PaymentService
 * @return Result - PRE_AUTHORISED or UNABLE_TO_AUTHORISE
 *
 */
public class PaymentDetailPreAuthorisationHandler implements RequestStreamHandler, RequestHandler<Object, Object> {

  @Override
  public Object handleRequest(Object input, Context context) {
    return null;
  }

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

    String result = "UNABLE_TO_AUTHORISE";

    try {
      

    CreditCardPaymentDetail creditCardPaymentDetail = new Gson().fromJson(IOUtils.toString(inputStream, "UTF-8"),
        CreditCardPaymentDetail.class);

    context.getLogger().log("creditCardPaymentDetail: " + creditCardPaymentDetail);

    preAuthorisePayment(creditCardPaymentDetail);
    result = "PRE_AUTHORISED";
    } catch (Exception e) {
      context.getLogger().log("Exception: " + e);
    }
    context.getLogger().log("writing to stream: " + result);
    outputStream.write(result.getBytes());

  }
  
  //do nothing for POC
  private void preAuthorisePayment(CreditCardPaymentDetail creditCardPaymentDetail) {
    
  }

}
