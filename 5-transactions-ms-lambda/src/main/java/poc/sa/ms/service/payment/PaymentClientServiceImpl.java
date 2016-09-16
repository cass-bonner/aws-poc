package poc.sa.ms.service.payment;

import java.io.IOException;

import com.amazonaws.services.apigateway.AmazonApiGatewayClient;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import poc.sa.ms.model.order.payment.CreditCardPaymentDetail;

public class PaymentClientServiceImpl implements PaymentClientService {
  
  public final static String PAYMENT_ENDPOINT="https://jcbxzdz84b.execute-api.ap-northeast-1.amazonaws.com/dev";

  public final static String PAYMENT_VERIFY_ENDPOINT=PAYMENT_ENDPOINT + "/verify";
  public final static String PAYMENT_PREAUTH_ENDPOINT=PAYMENT_ENDPOINT + "/preauthorise";

  public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

  OkHttpClient client = new OkHttpClient();

  String post(String url, String json) throws IOException {
    RequestBody body = RequestBody.create(JSON, json);
    Request request = new Request.Builder().url(url).post(body).build();
    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }



  @Override
  public void verify(CreditCardPaymentDetail creditCardPaymentPaymentDetail) {
    String response = "";
    try {
      response = post(PAYMENT_VERIFY_ENDPOINT,new Gson().toJson(creditCardPaymentPaymentDetail,CreditCardPaymentDetail.class));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println("Response: " + response);
    if (response == null || !response.equals("VALID")) {
      throw new RuntimeException("Unable to verify payment details; Please check details and re-submit.");
    }
  }

  @Override
  public void preAuth(CreditCardPaymentDetail creditCardPaymentPaymentDetail) {

    if (creditCardPaymentPaymentDetail.getNetPaymentAmount().floatValue() > 100) {
      String response = "";
      try {
        response = post(PAYMENT_PREAUTH_ENDPOINT,new Gson().toJson(creditCardPaymentPaymentDetail,CreditCardPaymentDetail.class));
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      System.out.println("Response: " + response);
      if (response == null || !response.equals("PRE_AUTHORISED")) {
        throw new RuntimeException("Unable to obtain pre-authorisation.");
      }
    }
  }

}
