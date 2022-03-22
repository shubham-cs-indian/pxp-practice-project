package com.cs.core.runtime.strategy.usecase.loadbalancer.ribbon;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.AbstractConfiguration;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.netflix.client.ClientFactory;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

public class OrientDBLoadBalancer {
  
  protected static AbstractConfiguration         configInstance;
  protected static DefaultClientConfigImpl       clientConfig;
  protected static CSLoadBalancerAwareClient     orientClient;
  protected static ILoadBalancer                 orientLoadBalancer;
  
  protected static HashMap<String, List<Server>> orientMap;
  
  static {
    try {
      initializeLoadBalancer();
    }
    catch (Exception e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
  }
  
  public static void initializeLoadBalancer() throws Exception
  {
    loadDefaultProperties();
    overwriteConfiguredProperties();
    // orientMap = new HashMap<>();
    orientClient = (CSLoadBalancerAwareClient) ClientFactory.getNamedClient("orient-loadbalancer");
    orientLoadBalancer = orientClient.getLoadBalancer();
  }
  
  public static HashMap<String, List<Server>> getOrientMap()
  {
    return orientMap;
  }
  
  public static CSLoadBalancerAwareClient getOrientClient() throws Exception
  {
    if (orientClient == null) {
      synchronized (orientClient) {
        if (orientClient == null) {
          initializeLoadBalancer();
        }
      }
    }
    return orientClient;
  }
  
  public static ILoadBalancer getOrientLoadBalancer()
  {
    return orientLoadBalancer;
  }
  
  public static void setConfigInstance(AbstractConfiguration configInstance) throws Exception
  {
    OrientDBLoadBalancer.configInstance = configInstance;
  }
  
  public static void loadDefaultProperties() throws Exception
  {
    DefaultClientConfigImpl clientConfig = new DefaultClientConfigImpl("ribbon");
    clientConfig.loadProperties("orient-loadbalancer");
    setClientConfig(clientConfig);
  }
  
  public static void overwriteConfiguredProperties() throws Exception
  {
    ConfigurationManager.loadPropertiesFromResources("orient-loadbalancer.properties");
    AbstractConfiguration orientConfigInstance = ConfigurationManager.getConfigInstance();
    setConfigInstance(orientConfigInstance);
  }
  
  public AbstractConfiguration getConfigurationInstance() throws Exception
  {
    return configInstance;
  }
  
  public DefaultClientConfigImpl getClientConfig()
  {
    return clientConfig;
  }
  
  public static void setClientConfig(DefaultClientConfigImpl clientConfig)
  {
    OrientDBLoadBalancer.clientConfig = clientConfig;
  }
}
