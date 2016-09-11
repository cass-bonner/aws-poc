package poc.sa.ms.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import poc.sa.ms.exception.InvalidRequestException;

public class ActionController {


	public static void lambdaHandleRequest(InputStream request, OutputStream response, Context context) throws InvalidRequestException {
		LambdaLogger logger = context.getLogger();

        JsonParser parser = new JsonParser();
        JsonObject inputObj;
        try {
            inputObj = parser.parse(IOUtils.toString(request)).getAsJsonObject();
        } catch (IOException e) {
            logger.log("Error while reading request\n" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        
        if (inputObj == null || inputObj.get("action") == null || inputObj.get("action").getAsString().trim().equals("")) {
            logger.log("Invald inputObj, could not find action parameter");
            throw new InvalidRequestException("Could not find action value in request");
        }
        String actionClass = inputObj.get("action").getAsString();
        Action action;

        try {
            action = Action.class.cast(Class.forName(actionClass).newInstance());
        } catch (final InstantiationException e) {
            logger.log("Error while instantiating action class\n" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (final IllegalAccessException e) {
            logger.log("Illegal access while instantiating action class\n" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (final ClassNotFoundException e) {
            logger.log("Action class could not be found\n" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        if (action == null) {
            logger.log("Action class is null");
            throw new InvalidRequestException("Invalid action class");
        }

        JsonObject body = null;
        if (inputObj.get("body") != null) {
            body = inputObj.get("body").getAsJsonObject();
        }

        String output = action.handle(body, context);

        try {
            IOUtils.write(output, response);
        } catch (final IOException e) {
            logger.log("Error while writing response\n" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
	
	}
}
