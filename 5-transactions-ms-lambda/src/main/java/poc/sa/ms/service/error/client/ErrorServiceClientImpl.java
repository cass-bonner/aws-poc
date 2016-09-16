package poc.sa.ms.service.error.client;

import java.io.IOException;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import poc.sa.ms.model.order.Order;

public class ErrorServiceClientImpl implements ErrorServiceClient {
  
  public final static String ERROR_SERVICE_ENDPOINT="https:///dev";

  public final static String ES_PROCESS_ENDPOINT=ERROR_SERVICE_ENDPOINT + "/process";

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
  public void process(Order order, String serviceId, String messageDetail) {
    String response = "";
    ErrorWrapper errorWrapper = new ErrorWrapper();
    errorWrapper.setMessageDetail(messageDetail);
    errorWrapper.setOrder(order);
    errorWrapper.setServiceId(serviceId);
    try {
      response = post(ERROR_SERVICE_ENDPOINT,new Gson().toJson(errorWrapper));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println("Response: " + response);
    if (response == null || !response.equals("VALID")) {
      throw new RuntimeException("Unable to verify payment details; Please check details and re-submit.");
    }
    
  }
  
  private class ErrorWrapper {
    private Order order;
    private String serviceId;
    private String messageDetail;
    public Order getOrder() {
      return order;
    }
    public void setOrder(Order order) {
      this.order = order;
    }
    public String getServiceId() {
      return serviceId;
    }
    public void setServiceId(String serviceId) {
      this.serviceId = serviceId;
    }
    public String getMessageDetail() {
      return messageDetail;
    }
    public void setMessageDetail(String messageDetail) {
      this.messageDetail = messageDetail;
    }
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + ((messageDetail == null) ? 0 : messageDetail.hashCode());
      result = prime * result + ((order == null) ? 0 : order.hashCode());
      result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
      return result;
    }
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      ErrorWrapper other = (ErrorWrapper) obj;
      if (!getOuterType().equals(other.getOuterType()))
        return false;
      if (messageDetail == null) {
        if (other.messageDetail != null)
          return false;
      } else if (!messageDetail.equals(other.messageDetail))
        return false;
      if (order == null) {
        if (other.order != null)
          return false;
      } else if (!order.equals(other.order))
        return false;
      if (serviceId == null) {
        if (other.serviceId != null)
          return false;
      } else if (!serviceId.equals(other.serviceId))
        return false;
      return true;
    }
    private ErrorServiceClientImpl getOuterType() {
      return ErrorServiceClientImpl.this;
    }
    @Override
    public String toString() {
      return "ErrorWrapper [order=" + order + ", serviceId=" + serviceId + ", messageDetail=" + messageDetail + "]";
    }
    
  }


}
