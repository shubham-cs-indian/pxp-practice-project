package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public interface IIdsListWithContentIdModel extends IModel {
  
  public static String IDS        = "ids";
  public static String CONTENT_ID = "contentId";
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
  
  public String getContentId();
  
  public void setContentId(String contentId);
}
