//package poc.sa.ms.control.user;
//
//import java.security.NoSuchAlgorithmException;
//import java.security.spec.InvalidKeySpecException;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.LambdaLogger;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonObject;
//
//import poc.sa.ms.control.Action;
//import poc.sa.ms.exception.DaoException;
//import poc.sa.ms.exception.ExceptionMessage;
//import poc.sa.ms.exception.InvalidRequestException;
//import poc.sa.ms.helper.PasswordHelper;
//import poc.sa.ms.model.DaoFactory;
//import poc.sa.ms.model.action.LoginUserRequest;
//import poc.sa.ms.model.action.LoginUserResponse;
//import poc.sa.ms.model.user.User;
//import poc.sa.ms.model.user.UserCredentials;
//import poc.sa.ms.model.user.UserDao;
//import poc.sa.ms.model.user.UserIdentity;
//import poc.sa.ms.provider.CredentialsProvider;
//import poc.sa.ms.provider.ProviderFactory;
//
//public class LoginUserAction implements Action {
//	    private LambdaLogger logger;
//	    private CredentialsProvider cognito = ProviderFactory.getCredentialsProvider();
//
//	    public String handle(JsonObject request, Context lambdaContext) throws InvalidRequestException {
//	        logger = lambdaContext.getLogger();
//
//	        LoginUserRequest input = getGson().fromJson(request, LoginUserRequest.class);
//
//	        if (input == null ||
//	                input.getUsername() == null ||
//	                input.getUsername().trim().equals("") ||
//	                input.getPassword() == null ||
//	                input.getPassword().trim().equals("")) {
//	            throw new InvalidRequestException(ExceptionMessage.INVALID_PAYLOAD);
//	        }
//
//	        UserDao dao = DaoFactory.getUserDao();
//	        User loggedUser;
//	        try {
//	            loggedUser = dao.getUserByName(input.getUsername());
//	        } catch (final DaoException e) {
//	            logger.log("Error while loading user\n" + e.getMessage());
//	            throw new RuntimeException(ExceptionMessage.EX_DAO_ERROR);
//	        }
//
//	        if (loggedUser == null) {
//	            throw new RuntimeException(ExceptionMessage.INVALID_PAYLOAD);
//	        }
//
//	        try {
//	            if (!PasswordHelper.authenticate(input.getPassword(), loggedUser.getPasswordBytes(), loggedUser.getSaltBytes())) {
//	                throw new InvalidRequestException(ExceptionMessage.INVALID_PAYLOAD);
//	            }
//	        } catch (final NoSuchAlgorithmException e) {
//	            logger.log("No algrithm found for password encryption\n" + e.getMessage());
//	            throw new RuntimeException(ExceptionMessage.EX_PWD_SALT);
//	        } catch (final InvalidKeySpecException e) {
//	            logger.log("No KeySpec found for password encryption\n" + e.getMessage());
//	            throw new RuntimeException(ExceptionMessage.EX_PWD_ENCRYPT);
//	        }
//
//	        UserIdentity identity;
//	        UserCredentials credentials;
//	        try {
//	            identity = cognito.getUserIdentity(loggedUser);
//	            loggedUser.setIdentity(identity);
//	            credentials = cognito.getUserCredentials(loggedUser);
//	        } catch (final RuntimeException e) {
//	            logger.log("Error while getting oidc token through Cognito\n" + e.getMessage());
//	            throw new RuntimeException(ExceptionMessage.EX_NO_COGNITO_IDENTITY);
//	        }
//
//	        // if we reach this point we assume that the user is authenticated.
//	        LoginUserResponse output = new LoginUserResponse();
//	        output.setIdentityId(loggedUser.getCognitoIdentityId());
//	        output.setToken(identity.getOpenIdToken());
//	        output.setCredentials(credentials);
//
//	        return getGson().toJson(output);
//	    }
//	    
//	    protected Gson getGson() {
//        return new GsonBuilder()
//                .setPrettyPrinting()
//                .create();
//    }
//	}
//
