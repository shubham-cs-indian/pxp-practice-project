package com.cs.core.runtime.strategy.model.loadbalancer;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ILoadBalancingModel extends IModel {
  
  String getURL();
  
  void setURL(String url);
  
  String getInitiatingStrategyName();
  
  void setInitiatingStrategyName(String initiatingStrategyName);
  
  String getRequestContent();
  
  void setRequestContent(String requestContent);
  
  String getMethodType();
  
  void setMethodType(String methodType);
}
