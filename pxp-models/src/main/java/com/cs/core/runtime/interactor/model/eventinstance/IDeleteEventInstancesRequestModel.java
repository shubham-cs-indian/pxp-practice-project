package com.cs.core.runtime.interactor.model.eventinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IDeleteEventInstancesRequestModel extends IModel {
  
  public static final String CONTENT_ID = "contentId";
  public static final String IDS        = "ids";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public List<String> getIds();
  
  public void setIds(List<String> Ids);
}
