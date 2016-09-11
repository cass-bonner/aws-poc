package poc.sa.ms.service.payment;

import org.junit.Test;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.apigateway.AmazonApiGatewayClient;
import com.amazonaws.services.apigateway.model.TestInvokeMethodRequest;
import com.amazonaws.services.apigateway.model.TestInvokeMethodResult;

import poc.sa.ms.control.order.OrderCaptureHandlerTest;

public class PaymentServiceImplTest {

  @Test
  public void test() {
    BasicAWSCredentials basicAwsCredentials = new BasicAWSCredentials("AKIAJVFELT2KUWMTOJTQ","jON3h8/b3Hu32VkzKllMZTR/6gHnA9L3oTimQ4Or");
    
    String json = OrderCaptureHandlerTest.generateSampleJson();
    AmazonApiGatewayClient amazonApiGatewayClient = new AmazonApiGatewayClient(basicAwsCredentials);
    amazonApiGatewayClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
    TestInvokeMethodRequest testInvokeMethodRequest = new TestInvokeMethodRequest();
    testInvokeMethodRequest.setRequestCredentials(basicAwsCredentials);
    testInvokeMethodRequest.setRestApiId("jcbxzdz84b");
    testInvokeMethodRequest.setResourceId("/verify");
    testInvokeMethodRequest.setBody(json);
    testInvokeMethodRequest.setHttpMethod("POST");
    
    TestInvokeMethodResult testInvokeMethodResult = amazonApiGatewayClient.testInvokeMethod(testInvokeMethodRequest);
    System.out.println("testInvokeMethodResult: " + testInvokeMethodResult);



  }

}
