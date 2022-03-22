package com.cs.core.config.interactor.model.globalpermissions;

import java.util.HashSet;
import java.util.Set;

public class GetGlobalPermissionForMultipleNatureTypesRequestModel
    implements IGetGlobalPermissionForMultipleNatureTypesRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Set<String>     klassIds         = new HashSet<>();
  protected String          userId;
  
  public Set<String> getKlassIds()
  {
    return klassIds;
  }
  
  public void setKlassIds(Set<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  public String getUserId()
  {
    return userId;
  }
  
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
}
