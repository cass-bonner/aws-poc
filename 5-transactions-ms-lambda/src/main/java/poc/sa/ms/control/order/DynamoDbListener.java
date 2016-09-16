package poc.sa.ms.control.order;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.google.gson.Gson;

import poc.sa.ms.daoconfig.SolutionConfiguration;
import poc.sa.ms.model.error.ErrorWrapper;
import poc.sa.ms.model.order.Order;

public class DynamoDbListener implements RequestHandler<DynamodbEvent, Object> {

    @Override
    public Object handleRequest(DynamodbEvent dynamoDbEvent, Context context) {
        context.getLogger().log("dynamoDbEvent: " + dynamoDbEvent);
        List<DynamodbStreamRecord> streams = dynamoDbEvent.getRecords();
        for (DynamodbStreamRecord dynamodbStreamRecord: streams) {
          
          Map<String, AttributeValue> newImage = dynamodbStreamRecord.getDynamodb().getNewImage();
          
          String sorderId="";
          AttributeValue orderId = newImage.get("orderId");
          if (orderId != null) {
             sorderId = orderId.getS();
          }
          context.getLogger().log("orderId: " + sorderId);
          
          AttributeValue jsonOrder = newImage.get("orderPayload");
          String sjsonOrder = "";
          if (jsonOrder != null) {
            sjsonOrder = jsonOrder.getS();
          }
          context.getLogger().log("jsonOrder::::: " + sjsonOrder);
          
          try {
            //marshall to order so we can set the orderId on the json and determine order value.
            Order order = new Gson().fromJson(sjsonOrder, Order.class);
            order.setOrderId(sorderId);
//            FulfillmentServiceClient fulfillmentServiceClient = new FulfillmentServiceClientImpl();
//            fulfillmentServiceClient.fulfill(order);
            
            AWSLambdaClient awsLambdaClient = new AWSLambdaClient();
            awsLambdaClient.setRegion(SolutionConfiguration.REGION);
            awsLambdaClient.setEndpoint(SolutionConfiguration.LAMBDA_ENDPOINT);
            
            boolean failedPreAuth = false;
            //perform the pre-auth for any orders less than 100  
            if (order.getCreditCardPaymentDetail().getNetPaymentAmount().intValue() <= 100) {
              context.getLogger().log("calcing pre auth: " + order.getOrderId());
              InvokeRequest invokeRequest = new InvokeRequest();
              invokeRequest = invokeRequest.withFunctionName("PaymentPreAuthorisationHandler")
                  .withPayload(new Gson().toJson(order.getCreditCardPaymentDetail()));
              
              InvokeResult invokeResult = awsLambdaClient.invoke(invokeRequest);
              context.getLogger().log("pre-auth invokeResult: " + invokeResult);
              String stringResult = new String(invokeResult.getPayload().array());
              if (invokeResult != null && invokeResult.getPayload() != null) {
                context.getLogger().log("order id: " + order.getOrderId() + " invokeResult.getPayload(): " +stringResult);
                if (!stringResult.equals("PRE_AUTHORISED")) {
                  
                  // invoke the Error Handler
                  InvokeRequest invokeRequest2 = new InvokeRequest();

                  ErrorWrapper errorWrapper = new ErrorWrapper();
                  errorWrapper.setOrder(new Gson().toJson(order));
                  errorWrapper.setMessageDetail("Order failed pre-authorisation payment. Please contact customer.");
                  errorWrapper.setServiceId("DynamoDbListener");

                  invokeRequest2 = invokeRequest2.withFunctionName("ErrorHandler")
                      .withPayload(new Gson().toJson(errorWrapper));
                  
                  InvokeResult invokeResult2 = awsLambdaClient.invoke(invokeRequest2);
                  
                  context.getLogger().log("ErrorHandler: " + invokeResult2);
                  failedPreAuth = true;
                } 
              }
              
            }
            
            if (!failedPreAuth) {
              InvokeRequest invokeRequest = new InvokeRequest();
              invokeRequest = invokeRequest.withFunctionName("FullfillmentHandler")
                  .withPayload(new Gson().toJson(order));
              
              InvokeResult invokeResult = awsLambdaClient.invoke(invokeRequest);
              context.getLogger().log("Fulfillment handler invokeResult: " + invokeResult);
            }
            
          } catch (Exception e) {
            context.getLogger().log("Exception thrown: " + e.getMessage());
            e.printStackTrace();
          }
        }

        return null;
    }

}