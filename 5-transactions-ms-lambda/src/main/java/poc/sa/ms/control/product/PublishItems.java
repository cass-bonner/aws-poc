package poc.sa.ms.control.product;

import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.model.S3Event;


// Pull over logic from s3 example.
public class PublishItems implements RequestHandler<S3Event, Object> {

    @Override
    public Object handleRequest(S3Event input, Context context) {

      
    // insert break down logic here. 
      
    // We prob want to use asynch instead - test it out.
      
    AWSLambdaClient awsLambdaClient = new AWSLambdaClient();
    InvokeRequest invokeRequest = new InvokeRequest();
    invokeRequest = invokeRequest.withFunctionName("PaymentValidation")
        .withPayload("insert sub ");
    
    
    InvokeResult invokeResult = awsLambdaClient.invoke(invokeRequest);
    return 1;
  }


  
}
