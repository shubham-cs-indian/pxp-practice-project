package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IStatusModel extends IModel {
  
  public static final String TOTAL_COUNT      = "totalCount";
  public static final String COMPLEATED_COUNT = "completedCount";
  public static final String FAILED_COUNT     = "failedCount";
  public static final String FAILED_IDS       = "failedIds";
  public static final String SUCCESS_IDS      = "successIds";
  public static final String PROGRESS_IDS     = "progressIds";
  public static final String FAILURELIST      = "failureList";
  
  public int getTotalCount();
  
  public void setTotalCount(int totalCount);
  
  public int getCompletedCount();
  
  public void setCompletedCount(int completedCount);
  
  public int getFailedCount();
  
  public void setFailedCount(int failedCount);
  
  public List<String> getFailedIds();
  
  public void setFailedIds(List<String> failedIds);
  
  public Map<String, String> getProgressIds();
  
  public void setProgressIds(Map<String, String> progressIds);
  
  public List<String> getSuccessIds();
  
  public void setSuccessIds(List<String> successIds);
  
  public List<Map<String, String>> getFailureList();
  
  public void setFailureList(List<Map<String, String>> failureList);
}
