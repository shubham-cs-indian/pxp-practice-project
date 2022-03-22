package com.cs.core.runtime.strategy.model.loadbalancer;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public class CSLoadBalancingModel implements ILoadBalancingModel, IModel {
  
  private static final long serialVersionUID = 1L;
  
  private String            url;
  
  private String            initiatingStrategyName;
  
  private String            methodType;
  
  private String            requestContent;
  
  public CSLoadBalancingModel(String url, String initiatingStrategyName, String methodType,
      String requestContent)
  {
    this.url = url;
    this.initiatingStrategyName = initiatingStrategyName;
    this.methodType = methodType;
    this.requestContent = requestContent;
  }
  
  @Override
  public String getURL()
  {
    return url;
  }
  
  @Override
  public void setURL(String url)
  {
    this.url = url;
  }
  
  @Override
  public String getInitiatingStrategyName()
  {
    return initiatingStrategyName;
  }
  
  @Override
  public void setInitiatingStrategyName(String initiatingStrategyName)
  {
    this.initiatingStrategyName = initiatingStrategyName;
  }
  
  @Override
  public String getRequestContent()
  {
    return requestContent;
  }
  
  @Override
  public void setRequestContent(String requestContent)
  {
    this.requestContent = requestContent;
  }
  
  @Override
  public String getMethodType()
  {
    return methodType;
  }
  
  @Override
  public void setMethodType(String methodType)
  {
    this.methodType = methodType;
  }
}
