package com.cs.core.config.interactor.model.mapping;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetMappedMappingRequestModel extends IModel {
  
  public static final String FILE_HEADERS    = "fileHeaders";
  public static final String CURRENT_USER_ID = "currentUserId";
  
  public List<String> getFileHeaders();
  
  public void setFileHeaders(List<String> fileHeaders);
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String userId);
}
