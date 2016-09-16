# Setup Microservices Lambda POC

## Introduction
The microservices lambda POC is an application built in Java for [AWS Lambda](http://aws.amazon.com/lambda/). It uses [Amazon API Gateway](http://aws.amazon.com/api-gateway/) to expose the Lambda function as HTTP endpoints and uses Identity and Access Management (IAM).

At this pointn it is not using [Amazon Cognito](http://aws.amazon.com/cognito/), but that may be integrated if we build out a UI. For now it can be invoked via Postman with AWS credentials specified.

The solution consists of:

* OrderService (API Gateway) which invokes Lambda Function OrderCaptureHandler for order creation /orders POST
* Payment Service (API Gateway) - called by OrderService synchronously during order submission if the order is > 100. If <= 100 - then it is called by the event-driven  DynamoDbListener in an asynchronous manner
DbListener w asynchronous
* DynamoDbListener - Lambda function deployed to DynamoDb Order table that invokes
* FullfillmentHandler - Lambda function that manages the stock inventory. It will report various errors to the ErrorHandler as they occur such as invalid SKUs, out of stock, etc. Items are removed from orders if they can not be fulfilled and the customer will be notified.
* ErrorHandler - Logs the details of the error and the full order state to the Error Table.


## POC Setup
##DynamoDB
* The current version of the solution requires three DynamoDB table be created as follows:

** Order - should have only a `Hash Key` of type `string` called **orderId**.  This is to store the The annotated objects is on `poc.sa.ms.model.order.PersistedOrder` which stores a generated orderId as a key and an `poc.sa.ms.model.order.Order` as a json string.
** ItemStock - **sku** for `Hash Key` (string), **quantity** as int, **reOrderd"" as boolean. This table contains the stock inventory for a given SKU and is deducted as orders are placed.
 * Error - **errorId** for tracking errors. the remaining attributes will be built out by the service.
 
you can use the dynamodb.cf file for Order at src/main/resources but then you will need to update the code with the appropriate table name.
 


## Build and Deploy the Application to AWS Lambda

The application will need to be modified to have the appropriate table name (if you did not use Order) and as well the REST APIs for the Payment Service endpoints. This have not yet been created
 

* Now that the application is configured you can build it and package it for AWS Lambda using [Maven](https://maven.apache.org/). Either through your IDO or by opening a terminal run `mvn install`. This will create a *target* directory and inside it a file called `ms-lambda-0.0.7-SNAPSHOT.jar`.

* Due to the size of the jar file - you will need to upload it to s3 and then refer to that whilst creating the Lambda functions. Please do that now and make a note of the URL path to the file. it will need to be in the same region as your other services.

* Now we need to create AWS Lambda function that needs access to the resources created above. Create a new role in AWS Identity and Access Management with the following policies:
 
* Open the AWS Lambda console and create a new function. Skip the blueprint selection page and go straight to the *Configure Function* step. In this screen give your function a name and select *Java 8* as runtime. AWS Lambda will ask you to upload a ZIP file for your function. 

Create two roles - one for OrderCaptureHandler and another for the Payment functions. Both will require CloudWatch access to write logs. Furhter, The OrderCaptureHandler will require a policy to write to DynamoDb such as:

   ```json
   {
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "Stmt1473558668000",
            "Effect": "Allow",
            "Action": [
                "dynamodb:BatchGetItem",
                "dynamodb:BatchWriteItem",
                "dynamodb:DeleteItem",
                "dynamodb:DescribeReservedCapacity",
                "dynamodb:DescribeReservedCapacityOfferings",
                "dynamodb:DescribeStream",
                "dynamodb:DescribeTable",
                "dynamodb:GetItem",
                "dynamodb:GetRecords",
                "dynamodb:GetShardIterator",
                "dynamodb:ListStreams",
                "dynamodb:ListTables",
                "dynamodb:PurchaseReservedCapacityOfferings",
                "dynamodb:PutItem",
                "dynamodb:Query",
                "dynamodb:Scan",
                "dynamodb:UpdateItem",
                "dynamodb:UpdateTable"
            ],
            "Resource": [
                "arn:aws:dynamodb:ap-northeast-1:XXXXXXXXXXXX:table/Order"
            ]
        }
    ]
}
```
Create the following functions - all with java 8 and your jar file uploaded to s3:

###OrderCaptureHandler

* As a Handler for your function enter `poc.sa.ms.control.order.OrderCaptureHandler::handleRequest`.
* Use the order execution role created in the previous step.

###PaymentDetailsVerificationHandler

* As a Handler for your function enter `poc.sa.ms.control.order.payment.PaymentDetailVerificationHandler::handleRequest`.
* Use the payment role created in the previous step.

###PaymentPreAuthorisationHandler

* As a Handler for your function enter `poc.sa.ms.control.order.payment.PaymentDetailPreAuthorisationHandler::handleRequest`.
* Use the payment role created in the previous step.

	
###Creating the API Gateway APIs
* Now that the Lambda function is ready we can setup the API structure in Amazon API Gateway. To easily create the entire API we are going to use the Swagger format and import this into Amazon API Gateway.

 You will need to create all the services required by navigating to API gateway, creating a new Service, and uploading the yaml file as followings:
 
* OrderService: NB This will need to be modified with your function ARN once it is created. Open the Swagger definition in the `src/main/resources/order_capture_swag.yaml` file. Search the file for `x-amazon-apigateway-integration`. This tag defines the integration points between API Gateway and the backend, our Lambda function. Make sure that the `uri` for the Lambda function is correct, it should look like this:
```
arn:aws:apigateway:<YOUR REGION>:lambda:path/2015-03-31/functions/<YOUR LAMBDA FUNCTION ARN>/invocations

```
* PaymentService: Repeat the same for PaymentService using `src/main/resources/payment_swag.yaml`


* Once you have modified and saved the Swagger file to call the correct Lambda function and use your roles [create a new API in Amazon API Gateway](https://console.aws.amazon.com/apigateway/home?region=us-east-1#/apis/create) with the **Import from Swagger** feature.
* You should now be able to deploy and test your **Microservices Lamda OrderService** API with Amazon API Gateway using Postman. You will need to fill in the AWS credentials until we get a proper client established.

