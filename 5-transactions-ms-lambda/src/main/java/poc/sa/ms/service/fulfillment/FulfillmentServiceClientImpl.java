package poc.sa.ms.service.fulfillment;

import java.io.IOException;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import poc.sa.ms.model.order.Order;

public class FulfillmentServiceClientImpl implements FulfillmentServiceClient {

  public final static String FULFILLMENT_SERVICE_ENDPOINT="https://sbqzdxh3il.execute-api.ap-northeast-1.amazonaws.com/dev";

  public final static String FULFILL_PROCESS_ENDPOINT=FULFILLMENT_SERVICE_ENDPOINT + "/process";

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
  public void fulfill(Order order) {
    String response = "";
    try {
      response = post(FULFILL_PROCESS_ENDPOINT,new Gson().toJson(order));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println("Response: " + response);    
  }
   
}
