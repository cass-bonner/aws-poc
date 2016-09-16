package poc.sa.ms.daoconfig;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class SolutionConfiguration {
    // TODO: Specify the identity pool id
    public static final String IDENTITY_POOL_ID = "ap-northeast-1:69a49346-62b8-420a-ac97-a0989141ab85";
    // TODO: Specify the custom provider name used by the identity pool
    public static final String CUSTOM_PROVIDER_NAME = "APIGatewayTest"; //"com.customprovider";

    // This should not be changed, it is a default value for Cognito.
    public static final String COGNITO_PROVIDER_NAME = "cognito-identity.amazonaws.com";
    
    public static final Region REGION = Region.getRegion(Regions.AP_NORTHEAST_1);
    public static final String LAMBDA_ENDPOINT = "lambda.ap-northeast-1.amazonaws.com";
}