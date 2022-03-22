package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IDeleteTaskInstancesRequestModel extends IModel {
  
  public static final String CONTENT_ID       = "contentId";
  public static final String IDS              = "ids";
  public static final String IS_FOR_DASHBOARD = "isForDashboard";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public List<String> getIds();
  
  public void setIds(List<String> Ids);
  
  public Boolean getIsForDashboard();
  
  public void setIsForDashboard(Boolean isForDashboard);
}
