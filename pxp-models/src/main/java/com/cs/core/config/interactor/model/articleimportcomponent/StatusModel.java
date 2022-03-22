package com.cs.core.config.interactor.model.articleimportcomponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusModel implements IStatusModel {
  
  private static final long           serialVersionUID = 1L;
  protected int                       totalCount       = 0;
  protected int                       completedCount   = 0;
  protected int                       failedCount      = 0;
  protected List<String>              failedIds        = new ArrayList<>();
  protected List<String>              successIds       = new ArrayList<>();
  protected Map<String, String>       progressIds      = new HashMap<>();
  protected List<Map<String, String>> failureList      = new ArrayList<>();
  
  @Override
  public int getTotalCount()
  {
    return totalCount;
  }
  
  @Override
  public void setTotalCount(int totalCount)
  {
    this.totalCount = totalCount;
  }
  
  @Override
  public int getCompletedCount()
  {
    return completedCount;
  }
  
  @Override
  public void setCompletedCount(int completedCount)
  {
    this.completedCount = completedCount;
  }
  
  @Override
  public int getFailedCount()
  {
    return failedCount;
  }
  
  @Override
  public void setFailedCount(int failedCount)
  {
    this.failedCount = failedCount;
  }
  
  @Override
  public List<String> getFailedIds()
  {
    return failedIds;
  }
  
  @Override
  public void setFailedIds(List<String> failedIds)
  {
    this.failedIds = failedIds;
  }
  
  @Override
  public List<String> getSuccessIds()
  {
    return successIds;
  }
  
  @Override
  public void setSuccessIds(List<String> successIds)
  {
    this.successIds = successIds;
  }
  
  @Override
  public Map<String, String> getProgressIds()
  {
    
    return progressIds;
  }
  
  @Override
  public void setProgressIds(Map<String, String> progressIds)
  {
    this.progressIds = progressIds;
  }
  
  @Override
  public List<Map<String, String>> getFailureList()
  {
    return failureList;
  }
  
  @Override
  public void setFailureList(List<Map<String, String>> failureList)
  {
    this.failureList = failureList;
  }
}
