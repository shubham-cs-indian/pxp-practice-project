package com.cs.core.config.interactor.model.globalpermissions;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetGlobalPermissionForMultipleInstancesRequestModel
    implements IGetGlobalPermissionForMultipleInstancesRequestModel {
  
  private static final long                        serialVersionUID = 1L;
  
  protected String                                 userId;
  protected List<IGetGlobalPermissionRequestModel> requestList;
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public List<IGetGlobalPermissionRequestModel> getRequestList()
  {
    return requestList;
  }
  
  @JsonDeserialize(contentAs = GetGlobalPermissionRequestModel.class)
  @Override
  public void setRequestList(List<IGetGlobalPermissionRequestModel> requestList)
  {
    this.requestList = requestList;
  }
}
