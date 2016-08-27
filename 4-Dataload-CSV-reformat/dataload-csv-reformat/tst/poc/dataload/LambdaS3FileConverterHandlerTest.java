package poc.dataload;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LambdaS3FileConverterHandlerTest {

    private static S3Event input;
    private static Logger logger = Logger.getLogger(LambdaS3FileConverterHandlerTest.class);

    @BeforeClass
    public static void createInput() throws IOException {
    	System.out.println("creating input");
        input = TestUtils.parse("s3-event.put.json", S3Event.class);
        System.out.println("created input");
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        ctx.setFunctionName("fn");

        return ctx;
    }

    @Test
    public void testLambdaS3FileConverterHandler() {
    	logger.debug("creating handler..");
    	LambdaS3FileConverterHandler handler = new LambdaS3FileConverterHandler();
        Context ctx = createContext();

        logger.debug("calling handleRequest..");
        
        Object output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        if (output != null) {
            System.out.println(output.toString());
        }
    }
    
    /**
     * Test passes by nature of not throwing exception for valid scenario.
     */
    @Test
    public void testValidateValid() {

    	Map<String,String> footerValues = new HashMap<String,String>();
    	
    	//valid for 1 entry
    	footerValues.put(LambdaS3FileConverterHandler.FIELD_NAME,LambdaS3FileConverterHandler.FOOTER_INDICATOR);
    	footerValues.put(LambdaS3FileConverterHandler.FIELD_VALUE,"1");
    	List<HashMap<String, String>> csvAsMap = new ArrayList<HashMap<String,String>>();
    	Map<String,String> row = new HashMap<String,String>();
    	row.put(LambdaS3FileConverterHandler.FIELD_NAME, "metric1");
    	row.put(LambdaS3FileConverterHandler.FIELD_VALUE, "7");
    	csvAsMap.add((HashMap<String, String>) row);
    	
    	LambdaS3FileConverterHandler lambdaS3FileConverterHandler = new LambdaS3FileConverterHandler();
    	lambdaS3FileConverterHandler.validateFile(footerValues, csvAsMap);

    }
    @Test
    public void testValidateBadFooter() {

    	// incorrect count
    	Map<String,String> footerValues = new HashMap<String,String>();
    	try {
    		
        	footerValues.put(LambdaS3FileConverterHandler.FIELD_NAME,LambdaS3FileConverterHandler.FOOTER_INDICATOR);
        	footerValues.put(LambdaS3FileConverterHandler.FIELD_VALUE,"5");
        	List<HashMap<String, String>> csvAsMap = new ArrayList<HashMap<String,String>>();
        	Map<String,String> row = new HashMap<String,String>();
        	row.put(LambdaS3FileConverterHandler.FIELD_NAME, "metric1");
        	row.put(LambdaS3FileConverterHandler.FIELD_VALUE, "7");
        	csvAsMap.add((HashMap<String, String>) row);
        	
        	LambdaS3FileConverterHandler lambdaS3FileConverterHandler = new LambdaS3FileConverterHandler();
        	lambdaS3FileConverterHandler.validateFile(footerValues, csvAsMap);
    		
    	} catch (RuntimeException e) {
    		//received exception as expected.
    		assertEquals("Footer Values size not equal to number of rows",e.getMessage());
    		
    	}
    	// map not populated.
    	footerValues = new HashMap<String,String>();
    	try {
    		
        	List<HashMap<String, String>> csvAsMap = new ArrayList<HashMap<String,String>>();
        	Map<String,String> row = new HashMap<String,String>();
        	row.put(LambdaS3FileConverterHandler.FIELD_NAME, "metric1");
        	row.put(LambdaS3FileConverterHandler.FIELD_VALUE, "7");
        	csvAsMap.add((HashMap<String, String>) row);
        	
        	LambdaS3FileConverterHandler lambdaS3FileConverterHandler = new LambdaS3FileConverterHandler();
        	lambdaS3FileConverterHandler.validateFile(footerValues, csvAsMap);
    		
    	} catch (RuntimeException e) {
    		//received exception as expected.
    		assertEquals("Footer Values were not found - file may be corrupt or missing data",e.getMessage());
    		
    	}
    	
    	

    }
}
