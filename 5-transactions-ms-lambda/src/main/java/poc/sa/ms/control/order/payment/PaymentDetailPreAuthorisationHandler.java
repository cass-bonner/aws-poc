package poc.sa.ms.control.order.payment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import poc.sa.ms.model.order.payment.CreditCardPaymentDetail;

/**
 * Lamda function that pre authorises the payment if required.
 * 
 * @see poc.sa.ms.service.payment.PaymentClientService
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

    boolean valid = preAuthorisePayment(creditCardPaymentDetail);
    if (valid) {
      result = "PRE_AUTHORISED";
    }
    
    } catch (Exception e) {
      context.getLogger().log("Exception: " + e);
    }
    context.getLogger().log("writing to stream: " + result);
    outputStream.write(result.getBytes());

  }
  
  //for POC - if expiry date is in past it fails
  private boolean preAuthorisePayment(CreditCardPaymentDetail creditCardPaymentDetail) {
    boolean valid = true;
    if (creditCardPaymentDetail.getExpiryDate().before(new Date())) {
      System.out.println("Expiry Date is before current time it is invalid");
      valid = false; 
    }
    return valid;
  }

}
