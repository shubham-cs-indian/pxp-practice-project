package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IJMSImportTaxanomyStatusModel extends IModel {
  
  public static final String SUCCESS_IDS = "successIds";
  public static final String FAILED_IDS  = "failedIds";
  public static final String FAILURELIST = "failureList";
  
  public List<String> getFailedIds();
  
  public void setFailedIds(List<String> failedIds);
  
  public List<String> getSuccessIds();
  
  public void setSuccessIds(List<String> successIds);
  
  public List<Map<String, String>> getFailureList();
  
  public void setFailureList(List<Map<String, String>> failureList);
}
