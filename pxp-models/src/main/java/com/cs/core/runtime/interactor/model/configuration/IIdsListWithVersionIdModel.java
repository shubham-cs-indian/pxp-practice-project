package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public interface IIdsListWithVersionIdModel extends IModel {
  
  public static String IDS        = "ids";
  public static String VERSION_ID = "versionId";
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
  
  public Long getVersionId();
  
  public void setVersionId(Long versionId);
}
