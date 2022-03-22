package com.cs.core.config.interactor.model.grideditpropertylist;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetGridEditPropertyListRequestModel extends IConfigGetAllRequestModel {
  
  public static final String IS_RUNTIME_REQUEST = "isRuntimeRequest";
  
  public Boolean getIsRuntimeRequest();
  
  public void setIsRuntimeRequest(Boolean isRuntimeRequest);
}
