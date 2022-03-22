package com.cs.core.runtime.strategy.usecase.loadbalancer.ribbon;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.AbstractConfiguration;

import com.netflix.client.ClientFactory;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

public class ElasticLoadBalancer {
  
  protected static AbstractConfiguration     configInstance;
  protected static DefaultClientConfigImpl   clientConfig;
  protected static CSLoadBalancerAwareClient elasticClient;
  protected static ILoadBalancer             elasticLoadBalancer;
  
  protected static HashMap<String,List<Server>> elasticMap;
  
  public static HashMap<String, List<Server>> getElasticMap()
  {
    return elasticMap;
  }
  
  static {
    try {
      initializeLoadBalancer();
    }
    catch (Exception e) {
      // TODO: handle exception
    }
  }
  
  public static CSLoadBalancerAwareClient getElasticClient() throws Exception
  {
    if (elasticClient == null) {
      synchronized (elasticClient) {
        if (elasticClient == null) {
          initializeLoadBalancer();
        }
      }
    }
    return elasticClient;
  }
  
  public static ILoadBalancer getElasticLoadBalancer()
  {
    return elasticLoadBalancer;
  }
  
  public static void initializeLoadBalancer() throws Exception
  {
    
    loadDefaultProperties();
    overwriteConfiguredProperties();
    // elasticMap = new HashMap<>();
    elasticClient = (CSLoadBalancerAwareClient) ClientFactory
        .getNamedClient("elastic-loadbalancer");
    elasticLoadBalancer = elasticClient.getLoadBalancer();
  }
  
  public static void setConfigInstance(AbstractConfiguration configInstance) throws Exception
  {
    ElasticLoadBalancer.configInstance = configInstance;
  }
  
  public static void loadDefaultProperties() throws Exception
  {
    DefaultClientConfigImpl clientConfig = new DefaultClientConfigImpl("ribbon");
    clientConfig.loadProperties("elastic-loadbalancer");
    setClientConfig(clientConfig);
  }
  
  public static void overwriteConfiguredProperties() throws Exception
  {
    ConfigurationManager.loadPropertiesFromResources("elastic-loadbalancer.properties");
    AbstractConfiguration elasticConfigInstance = ConfigurationManager.getConfigInstance();
    setConfigInstance(elasticConfigInstance);
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
    ElasticLoadBalancer.clientConfig = clientConfig;
  }
}
