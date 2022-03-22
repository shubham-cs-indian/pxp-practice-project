package com.cs.core.config.interactor.model.processdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface ISourceDestinationResponseModel extends IModel {
  
  public static final String SOURCE_DESTINATION = "sourceDestination";
  
  public Map<String, String> getSourceDestination();
  
  public void setSourceDestination(Map<String, String> sourceDestination);
}
