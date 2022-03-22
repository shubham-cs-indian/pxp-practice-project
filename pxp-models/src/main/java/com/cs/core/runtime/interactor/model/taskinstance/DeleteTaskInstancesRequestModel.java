package com.cs.core.runtime.interactor.model.taskinstance;

import java.util.List;

public class DeleteTaskInstancesRequestModel implements IDeleteTaskInstancesRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          contentId;
  protected List<String>    ids;
  protected Boolean         isForDashboard   = false;
  
  @Override
  public String getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
  }
  
  @Override
  public List<String> getIds()
  {
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public Boolean getIsForDashboard()
  {
    return isForDashboard;
  }
  
  @Override
  public void setIsForDashboard(Boolean isForDashboard)
  {
    this.isForDashboard = isForDashboard;
  }
}
