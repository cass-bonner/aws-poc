package poc.sa.ms.control.order.payment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import poc.sa.ms.model.order.payment.CreditCardPaymentDetail;

/**
 * Lamda function that verifies the Payment Details to ensure they are valid. In this simple
 * example it uses local logic but in practice it would call out to the payment provider.
 * It returns 
 * 
 * @see poc.sa.ms.service.payment.PaymentClientService
 * @return Status of cc payment details- either INVALID or VALID
 *
 */
public class PaymentDetailVerificationHandler implements  RequestStreamHandler, RequestHandler<Object, Object> {

  @Override
  public Object handleRequest(Object input, Context context) {
      return null;
  }

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
    
    String status = "INVALID";

    CreditCardPaymentDetail creditCardPaymentDetail = new Gson().fromJson(IOUtils.toString(inputStream, "UTF-8"), CreditCardPaymentDetail.class); 

    context.getLogger().log("creditCardPaymentDetail: " + creditCardPaymentDetail);
    
    // quick check on the lenth 
    if (creditCardPaymentDetail != null && String.valueOf(creditCardPaymentDetail.getCreditCardNumber()).length()==16) {
      System.out.println("16 numbers");
      // none others are null
      if (creditCardPaymentDetail.getCreditCardProvider()!= null && creditCardPaymentDetail.getCreditCardProvider()!= null && 
          creditCardPaymentDetail.getNetPaymentAmount()!= null && creditCardPaymentDetail.getVerificationCode() != null) {
        context.getLogger().log("Status set to valid");
        status = "VALID";
        context.getLogger().log("Status set to valid");
      }
        
    }
    context.getLogger().log("writing to stream: " + status);
    outputStream.write(status.getBytes());
  }

}
