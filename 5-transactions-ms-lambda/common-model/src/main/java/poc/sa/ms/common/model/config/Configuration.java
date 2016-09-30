package poc.sa.ms.common.model.config;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

public class Configuration {

  private static Logger logger = Logger.getLogger(Configuration.class);

  private FileBasedConfiguration fileBasedConfiguration = null;
  
  public Configuration() {
    this("config.properties");
  }

  public Configuration(String fileName) {
    Parameters params = new Parameters();
    FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
        PropertiesConfiguration.class).configure(params.properties().setFileName(fileName));
    try {
      fileBasedConfiguration = builder.getConfiguration();
    } catch (ConfigurationException cex) {
      logger.error("Exception thrown loading property file config.properties: " + cex.getMessage(), cex);
    }
  }
  
  public String getStringProperty(String keyName) {
    return fileBasedConfiguration.getString(keyName);
  }

}
