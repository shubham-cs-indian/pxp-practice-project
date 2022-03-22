package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IIdsAndFailureIdsModel extends IModel {
  
  public static String IDS         = "ids";
  public static String FAILURE_IDS = "failureIds";
  
  public List<String> getIds();
  
  public void setIds(List<String> list);
  
  public List<String> getFailureIds();
  
  public void setFailureIds(List<String> list);
}
