package com.cs.core.config.interactor.model.grideditpropertylist;

import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;

public class GetGridEditablePropertyListRequestModel extends ConfigGetAllRequestModel
    implements IGetGridEditPropertyListRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Boolean         isRuntimeRequest = false;
  
  public Boolean getIsRuntimeRequest()
  {
    return isRuntimeRequest;
  }
  
  @Override
  public void setIsRuntimeRequest(Boolean isRuntimeRequest)
  {
    this.isRuntimeRequest = isRuntimeRequest;
  }
}
