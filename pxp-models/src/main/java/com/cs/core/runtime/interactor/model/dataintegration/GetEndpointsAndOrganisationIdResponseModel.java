package com.cs.core.runtime.interactor.model.dataintegration;

import java.util.Map;

public class GetEndpointsAndOrganisationIdResponseModel
    implements IGetEndpointsAndOrganisationIdResponseModel {
  
  private static final long   serialVersionUID = 1L;
  
  private Map<String, Object> processDefinitionInfo;
  private String              organisationId;
  
  @Override
  public Map<String, Object> getProcessDefinitionInfo()
  {
    return this.processDefinitionInfo;
  }
  
  @Override
  public void setProcessDefinitionInfo(Map<String, Object> processDefinitionInfo)
  {
    this.processDefinitionInfo = processDefinitionInfo;
  }
  
  @Override
  public String getOrganisationId()
  {
    return this.organisationId;
  }
  
  @Override
  public void setOrganisationId(String organisationId)
  {
    this.organisationId = organisationId;
  }
}
