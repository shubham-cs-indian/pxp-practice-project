package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public class IdsListWithContentIdModel implements IIdsListWithContentIdModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    ids;
  protected String          contentId;
  
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
  public String getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
  }
}
