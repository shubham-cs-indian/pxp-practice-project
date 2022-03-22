package com.cs.core.config.interactor.model.globalpermissions;

import java.util.List;

public class NatureKlassWithPermissionrequestModel
    implements INatureKlassWithPermissionrequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          userId;
  protected List<String>    klassIds;
  
  @Override
  public List<String> getKlassIds()
  {
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
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
}
