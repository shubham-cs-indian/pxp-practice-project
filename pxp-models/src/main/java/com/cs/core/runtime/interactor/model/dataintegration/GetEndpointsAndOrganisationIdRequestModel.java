package com.cs.core.runtime.interactor.model.dataintegration;

import java.util.List;

public class GetEndpointsAndOrganisationIdRequestModel
    implements IGetEndpointsAndOrganisationIdRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  private List<String>      processIds;
  private String            userId;
  
  @Override
  public List<String> getProcessIds()
  {
    return this.processIds;
  }
  
  @Override
  public void setProcessIds(List<String> processIds)
  {
    this.processIds = processIds;
  }
  
  @Override
  public String getUserId()
  {
    return this.userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
}
