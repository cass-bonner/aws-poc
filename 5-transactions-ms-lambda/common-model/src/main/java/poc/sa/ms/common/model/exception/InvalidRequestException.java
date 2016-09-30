package poc.sa.ms.common.model.exception;

public class InvalidRequestException extends RuntimeException {

	public InvalidRequestException(String invalidPayload) {
		super(invalidPayload);
	}
	public InvalidRequestException(String invalidPayload, Throwable t) {
		super(invalidPayload, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4078892697687505636L;

}
