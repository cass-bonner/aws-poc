package poc.dataload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class LambdaS3FileConverterHandler implements RequestHandler<S3Event, Object> {
	
	
	protected static final String TEST_BUCKET_NAME="cvstransformbucket23082016";
	protected static final String FOOTER_INDICATOR="#footer#";
	protected static final String FIELD_NAME="fieldName";
	protected static final String FIELD_VALUE="value";
	protected static final String FILE_NAME="dataload";

	@Override
    public Object handleRequest(S3Event s3Event, Context context) {
        context.getLogger().log("s3Event: " + s3Event);
        try {
            for (S3EventNotificationRecord record: s3Event.getRecords()) {
                String srcBucket = record.getS3().getBucket().getName();

                // Object key may have spaces or unicode non-ASCII characters.
                String srcKey = record.getS3().getObject().getKey()
                        .replace('+', ' ');
                srcKey = URLDecoder.decode(srcKey, "UTF-8");

                // Detect file type
                Matcher matcher = Pattern.compile(".*\\.([^\\.]*)").matcher(srcKey);
                if (!matcher.matches()) {
                    context.getLogger().log("Unable to detect file type for key " + srcKey);
                    return "";
                }
                String extension = matcher.group(1).toLowerCase();
                if (!"csv".equals(extension)) {
                    System.out.println("Skipping non-csv file " + srcKey + " with extension " + extension);
                    return "";
                }
                
                String fileName = srcKey.substring(0, srcKey.indexOf("."));
                context.getLogger().log("Transforming csv file " + srcBucket + "/" + srcKey);
                
                // Download the zip from S3 into a stream
                AmazonS3 s3Client = new AmazonS3Client();
                S3Object s3Object = s3Client.getObject(new GetObjectRequest(srcBucket, srcKey));
                
                CsvMapper mapper = new CsvMapper();
                CsvSchema schema = CsvSchema.emptySchema().withHeader(); // use first row as header
               
                MappingIterator<Map<String,String>> it = mapper.readerFor(Map.class)
                   .with(schema)
                   .readValues(s3Object.getObjectContent());
                
                
                List<HashMap<String, String>> csvAsMap = new ArrayList<HashMap<String,String>>();
    
                Map<String,String> footerValues = new HashMap<String,String>();
                while (it.hasNext()) {
                  HashMap<String,String> rowAsMap = (HashMap<String, String>) it.next();
             
                  context.getLogger().log("rowAsMap.get(FIELD_NAME): " + rowAsMap.get(FIELD_NAME));
                  // if this is the footer row, set the footer map.
                  if (rowAsMap.get(FIELD_NAME).equals(FOOTER_INDICATOR)) {
                	  footerValues = rowAsMap;
                  } else {
                	  csvAsMap.add(rowAsMap);
                  }
                  
                  // access by column name, as defined in the header row...
                }
                
                context.getLogger().log("footer values: " + footerValues);
                context.getLogger().log("csvAsMap: " + csvAsMap);
                
                validateFile(footerValues,csvAsMap);

                ByteArrayOutputStream os = new ByteArrayOutputStream();

                OutputStreamWriter writer = new OutputStreamWriter(os);
                
                CsvSchema scheman = null;
                CsvSchema.Builder schemaBuilder = CsvSchema.builder();
                if (csvAsMap != null && !csvAsMap.isEmpty()) {
                    for (String col : csvAsMap.get(0).keySet()) {
                        schemaBuilder.addColumn(col);
                    }
                    scheman = schemaBuilder.build().withLineSeparator("\r").withHeader();
                }
                CsvMapper mappern = new CsvMapper();
//                JsonMapper mappj = new JsonMapper();
                ObjectWriter objectWriter = mappern.writer(scheman);
//                objectWriter.
                
                SequenceWriter sequenceWriter = objectWriter.writeValues(writer);
                
                context.getLogger().log("csvAsMap.toArray(): " + csvAsMap.toArray());

                sequenceWriter.writeAll(csvAsMap.toArray());
                
                byte[] t = os.toByteArray();
                
                context.getLogger().log("t: " + t);
                ObjectMetadata meta = new ObjectMetadata();
				s3Client.putObject(srcBucket, fileName + "_v2.csv", new ByteArrayInputStream(t), meta);

				writer.flush();
                writer.close();
				
            }
            return "Ok";
        } catch (IOException e) {	
        	context.getLogger().log("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
	
	public static InputStream getStream(final CharSequence charSequence) {
		 return new InputStream() {
		  int index = 0;
		  int length = charSequence.length();
		  @Override public int read() throws IOException {
		   return index>=length ? -1 : charSequence.charAt(index++);
		  }
		 };
		}

	/**
	 * Example method for validation of file based on footer values.
	 * @param footerValues
	 * @param csvAsMap
	 * @return RuntimeException if any errors found in file.
	 */
	protected void validateFile(Map<String, String> footerValues, List<HashMap<String, String>> csvAsMap) {
		if (footerValues == null || footerValues.size() == 0) {
			throw new RuntimeException("Footer Values were not found - file may be corrupt or missing data");
		}
		if (!Integer.valueOf(footerValues.get(FIELD_VALUE)).equals(csvAsMap.size())) {
			throw new RuntimeException("Footer Values size not equal to number of rows");
		}
		
	}

}
