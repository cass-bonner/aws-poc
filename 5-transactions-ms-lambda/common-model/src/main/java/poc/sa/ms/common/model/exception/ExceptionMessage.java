package poc.sa.ms.common.model.exception;

public class ExceptionMessage {
	public static final String INVALID_PAYLOAD = "The request payload did not conform to the required json format. Please consult the APIs for appropriate use.";
	public static final String EX_PWD_SALT = "Cannot generate password salt";
    public static final String EX_PWD_ENCRYPT = "Failed to encrypt password";
    public static final String EX_PWD_SAVE = "Failed to save password";
    public static final String EX_NO_COGNITO_IDENTITY = "Cannot retrieve Cognito identity";
    public static final String EX_DAO_ERROR = "ErrorWrapper loading user or order";
}
