//package poc.sa.ms.provider;
//
//
//import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient;
//import com.amazonaws.services.cognitoidentity.model.GetCredentialsForIdentityRequest;
//import com.amazonaws.services.cognitoidentity.model.GetCredentialsForIdentityResult;
//import com.amazonaws.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityRequest;
//import com.amazonaws.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityResult;
//
//import poc.sa.ms.daoconfig.SolutionConfiguration;
//import poc.sa.ms.model.user.User;
//import poc.sa.ms.model.user.UserCredentials;
//import poc.sa.ms.model.user.UserIdentity;
//
//public class CognitoCredentialsProvider implements CredentialsProvider {
//    private static CognitoCredentialsProvider instance = null;
//
//    private static AmazonCognitoIdentityClient identityClient = new AmazonCognitoIdentityClient();
//
//    /**
//     * Gets the initialized instance of the CognitoCredentialsProvider. This provider should be accessed through the
//     * ProviderFactory and used through the CredentialsProvider interface.
//     *
//     * @return The singleton instance of the CognitoCredentialsProvider object
//     */
//    public static CognitoCredentialsProvider getInstance() {
//        if (instance == null) {
//            instance = new CognitoCredentialsProvider();
//        }
//        return instance;
//    }
//
//    protected CognitoCredentialsProvider() {
//        // protected constructor that should not be called outside the class
//    }
//
//    /**
//     * Retreives a set of AWS temporary credentials from Amazon Cognito using Developer Authenticated Identities.
//     *
//     * @param user The end user object. The identity property in the User object needs to be populated with a valid
//     *             identityId and openID Token
//     * @return A valid set of temporary AWS credentials
//     * @throws AuthorizationException
//     */
//    public UserCredentials getUserCredentials(User user) throws RuntimeException {
//        if (user == null || user.getCognitoIdentityId() == null || user.getCognitoIdentityId().trim().equals("")) {
//            throw new RuntimeException("Invalid user");
//        }
//
//        GetCredentialsForIdentityRequest credsRequest = new GetCredentialsForIdentityRequest();
//        credsRequest.setIdentityId(user.getCognitoIdentityId());
//        credsRequest.addLoginsEntry(
//                SolutionConfiguration.COGNITO_PROVIDER_NAME,
//                user.getIdentity().getOpenIdToken()
//        );
//
//        GetCredentialsForIdentityResult resp = identityClient.getCredentialsForIdentity(credsRequest);
//        if (resp == null) {
//            throw new RuntimeException("Empty GetCredentialsForIdentity response");
//        }
//
//        UserCredentials creds = new UserCredentials();
//        creds.setAccessKey(resp.getCredentials().getAccessKeyId());
//        creds.setSecretKey(resp.getCredentials().getSecretKey());
//        creds.setSessionToken(resp.getCredentials().getSessionToken());
//        creds.setExpiration(resp.getCredentials().getExpiration().getTime());
//
//        return creds;
//    }
//
//    /**
//     * Returns a Cognito IdentityID for the end user and a OpenID Token based on the custom developer authenticated
//     * identity provider configured in the SolutionConfiguration class. The OpenID token in the UserIdentity object is
//     * only used to retrieve credentials for the user with the getUserCredentials method and is very short lived.
//     *
//     * @param user The user that is logging in or registering
//     * @return A populated UserIdentity object.
//     * @throws AuthorizationException
//     */
//    public UserIdentity getUserIdentity(User user) throws RuntimeException {
//        if (user == null || user.getUsername() == null || user.getUsername().trim().equals("")) {
//            throw new RuntimeException("Invalid user");
//        }
//
//        GetOpenIdTokenForDeveloperIdentityRequest oidcRequest = new GetOpenIdTokenForDeveloperIdentityRequest();
//        oidcRequest.setIdentityPoolId(SolutionConfiguration.IDENTITY_POOL_ID);
//        if (user.getCognitoIdentityId() != null && !user.getCognitoIdentityId().trim().equals("")) {
//            oidcRequest.setIdentityId(user.getCognitoIdentityId());
//        }
//
//        oidcRequest.addLoginsEntry(
//                SolutionConfiguration.CUSTOM_PROVIDER_NAME,
//                user.getUsername()
//        );
//
//        GetOpenIdTokenForDeveloperIdentityResult resp = identityClient.getOpenIdTokenForDeveloperIdentity(oidcRequest);
//
//        if (resp == null) {
//            throw new RuntimeException("Empty GetOpenIdTokenForDeveloperIdentity response");
//        }
//
//        UserIdentity identity = new UserIdentity();
//        identity.setIdentityId(resp.getIdentityId());
//        identity.setOpenIdToken(resp.getToken());
//        return identity;
//    }
//}
