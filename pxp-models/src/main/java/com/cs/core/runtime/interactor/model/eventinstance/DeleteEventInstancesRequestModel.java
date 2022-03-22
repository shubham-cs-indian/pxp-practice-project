package com.cs.core.runtime.interactor.model.eventinstance;

import java.util.List;

public class DeleteEventInstancesRequestModel implements IDeleteEventInstancesRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          contentId;
  protected List<String>    ids;
  
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
}
