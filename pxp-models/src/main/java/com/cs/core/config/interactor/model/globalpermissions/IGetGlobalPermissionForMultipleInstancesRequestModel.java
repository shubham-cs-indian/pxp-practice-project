package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetGlobalPermissionForMultipleInstancesRequestModel extends IModel {
  
  public static final String USER_ID      = "userId";
  public static final String REQUEST_LIST = "requestList";
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public List<IGetGlobalPermissionRequestModel> getRequestList();
  
  public void setRequestList(List<IGetGlobalPermissionRequestModel> requestList);
}
