package com.cs.core.config.interactor.model.mapping;

import java.util.ArrayList;
import java.util.List;

public class GetMappedMappingRequestModel implements IGetMappedMappingRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    fileHeaders      = new ArrayList<>();
  protected String          currentUserId;
  
  @Override
  public List<String> getFileHeaders()
  {
    if (fileHeaders == null) {
      fileHeaders = new ArrayList<>();
    }
    return fileHeaders;
  }
  
  @Override
  public void setFileHeaders(List<String> fileHeaders)
  {
    this.fileHeaders = fileHeaders;
  }
  
  @Override
  public String getCurrentUserId()
  {
    return currentUserId;
  }
  
  @Override
  public void setCurrentUserId(String userId)
  {
    this.currentUserId = userId;
  }
}
