package poc.sa.ms.service.payment;

@com.amazonaws.mobileconnectors.apigateway.annotation.Service(endpoint = "https://jcbxzdz84b.execute-api.ap-northeast-1.amazonaws.com/dev")
public interface PaymentServiceClient {

    /**
     *
     *
     * @return Empty
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/verify", method = "POST")
    void verifyPost(String orderPaymentDetail);

}