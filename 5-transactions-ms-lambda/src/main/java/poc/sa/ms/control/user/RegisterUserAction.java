//package poc.sa.ms.control.user;
//
//import java.nio.ByteBuffer;
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
//import poc.sa.ms.model.action.RegisterUserRequest;
//import poc.sa.ms.model.action.RegisterUserResponse;
//import poc.sa.ms.model.user.User;
//import poc.sa.ms.model.user.UserDao;
//import poc.sa.ms.model.user.UserIdentity;
//import poc.sa.ms.provider.CredentialsProvider;
//import poc.sa.ms.provider.ProviderFactory;
//
//public class RegisterUserAction implements Action {
//
//    private CredentialsProvider cognito = ProviderFactory.getCredentialsProvider();
//
//    private LambdaLogger logger = null;
//
//    /**
//     * Handler implementation for the registration action. It expcts a RegisterUserRequest object in input and returns
//     * a serialized RegisterUserResponse object
//     *
//     * @param request       Receives a JsonObject containing the body content
//     * @param lambdaContext The Lambda context passed by the AWS Lambda environment
//     * @return Returns the new user identifier and a set of temporary AWS credentials as a RegisterUserResponse object
//     * @throws BadRequestException
//     * @throws InternalErrorException
//     */
//    public String handle(JsonObject request, Context lambdaContext) throws InvalidRequestException, RuntimeException {
//        logger = lambdaContext.getLogger();
//        RegisterUserRequest input = getGson().fromJson(request, RegisterUserRequest.class);
//
//        if (input == null ||
//                input.getUsername() == null ||
//                input.getUsername().trim().equals("") ||
//                input.getPassword() == null ||
//                input.getPassword().trim().equals("")) {
//            logger.log("Invalid input passed to " + this.getClass().getName());
//            throw new InvalidRequestException(ExceptionMessage.INVALID_PAYLOAD);
//        }
//
//        User newUser = new User();
//        newUser.setUsername(input.getUsername());
//
//        // encrypt password
//        try {
//            byte[] salt = PasswordHelper.generateSalt();
//            byte[] encryptedPassword = PasswordHelper.getEncryptedPassword(input.getPassword(), salt);
//            newUser.setPassword(ByteBuffer.wrap(encryptedPassword));
//            newUser.setSalt(ByteBuffer.wrap(salt));
//        } catch (final NoSuchAlgorithmException e) {
//            logger.log("No algrithm found for password encryption\n" + e.getMessage());
//            throw new RuntimeException(ExceptionMessage.EX_PWD_SALT);
//        } catch (final InvalidKeySpecException e) {
//            logger.log("No KeySpec found for password encryption\n" + e.getMessage());
//            throw new RuntimeException(ExceptionMessage.EX_PWD_ENCRYPT);
//        }
//
//        if (newUser.getPassword() == null) {
//            logger.log("Password null after encryption");
//            throw new RuntimeException(ExceptionMessage.EX_PWD_SAVE);
//        }
//
//        UserDao dao = DaoFactory.getUserDao();
//
//        UserIdentity identity;
//        try {
//            // check if the user exists
//            if (dao.getUserByName(newUser.getUsername()) != null) {
//                throw new InvalidRequestException("Username is taken");
//            }
//
//            identity = cognito.getUserIdentity(newUser);
//
//            if (identity == null || identity.getIdentityId() == null || identity.getIdentityId().trim().equals("")) {
//                logger.log("Could not load Cognito identity ");
//                throw new RuntimeException(ExceptionMessage.EX_NO_COGNITO_IDENTITY);
//            }
//
//            newUser.setIdentity(identity);
//            dao.createUser(newUser);
//        } catch (final DaoException e) {
//            logger.log("ErrorWrapper while saving new user\n" + e.getMessage());
//            throw new RuntimeException(ExceptionMessage.EX_DAO_ERROR);
//        } catch (final RuntimeException e) {
//            logger.log("ErrorWrapper while accessing Cognito\n" + e.getMessage());
//            throw new RuntimeException(ExceptionMessage.EX_NO_COGNITO_IDENTITY);
//        }
//
//        RegisterUserResponse output = new RegisterUserResponse();
//        output.setIdentityId(newUser.getCognitoIdentityId());
//        output.setUsername(newUser.getUsername());
//        output.setToken(identity.getOpenIdToken());
//        // get credentials
//        try {
//            output.setCredentials(cognito.getUserCredentials(newUser));
//        } catch (final RuntimeException e) {
//            // Credentials are not mandatory as the user can attempt to login again if they are missing,
//            // important that we tell them that the user is registered
//            logger.log("ErrorWrapper while accessing Cognito\n" + e.getMessage());
//        }
//
//        return getGson().toJson(output, RegisterUserResponse.class);
//    }
//    
//    protected Gson getGson() {
//      return new GsonBuilder()
//              .setPrettyPrinting()
//              .create();
//  }
//}
