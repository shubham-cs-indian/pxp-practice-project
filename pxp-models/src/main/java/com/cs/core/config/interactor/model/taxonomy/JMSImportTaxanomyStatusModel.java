package com.cs.core.config.interactor.model.taxonomy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JMSImportTaxanomyStatusModel implements IJMSImportTaxanomyStatusModel {
  
  private static final long           serialVersionUID = 1L;
  protected List<String>              failedIds        = new ArrayList<>();
  protected List<String>              successIds       = new ArrayList<>();
  protected List<Map<String, String>> failureList      = new ArrayList<>();
  
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
