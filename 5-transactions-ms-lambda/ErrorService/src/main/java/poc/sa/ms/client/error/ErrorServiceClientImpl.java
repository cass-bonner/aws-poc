package poc.sa.ms.client.error;

import org.apache.log4j.Logger;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaAsyncClient;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.google.gson.Gson;

import poc.sa.ms.common.model.config.Configuration;
import poc.sa.ms.common.model.order.Order;
import poc.sa.ms.common.model.order.OrderWorkflowState;
import poc.sa.ms.dao.error.ErrorWrapper;
import poc.sa.ms.handler.notification.NotificationService;
import poc.sa.ms.handler.notification.NotificationServiceImpl;

public class ErrorServiceClientImpl implements ErrorServiceClient {
  
  private static Logger logger = Logger.getLogger(ErrorServiceClientImpl.class);
  
  NotificationService notificationService = new NotificationServiceImpl();

  @Override
  public void process(ErrorWrapper errorWrapper) {
    
    
    AWSLambdaAsyncClient awsLambdaAsyncClient = new AWSLambdaAsyncClient();
    // Lambda defaults to us-east-1 so set it the current region
    Region region = Regions.getCurrentRegion();
    awsLambdaAsyncClient.setRegion(region);
    
    // invoke the Error Handler
    InvokeRequest invokeRequest = new InvokeRequest();
    
    Configuration configuration = new Configuration();

    invokeRequest = invokeRequest.withFunctionName(configuration.getStringProperty("errorHandlerFunctionName"))
        .withPayload(new Gson().toJson(errorWrapper));
    
    InvokeResult invokeResult = awsLambdaAsyncClient.invoke(invokeRequest);
    logger.info("ErrorServiceClient result in progress, current result: " + invokeResult);
    notificationService.notify(OrderWorkflowState.ORDER_MODIFICATION, errorWrapper.getOrder());
    
  }

  @Override
  public void process(Order order, String serviceId, String messageDetail) {
    
    ErrorWrapper errorWrapper = new ErrorWrapper();
    errorWrapper.setOrder(new Gson().toJson(order));
    errorWrapper.setMessageDetail(messageDetail);
    errorWrapper.setServiceId(serviceId);
    process(errorWrapper);
    
  }

}
