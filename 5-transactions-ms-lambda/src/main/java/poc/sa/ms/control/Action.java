package poc.sa.ms.control;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;

import poc.sa.ms.exception.InvalidRequestException;

public interface Action {
	String handle(JsonObject request, Context lambdaContext) throws InvalidRequestException;

}
