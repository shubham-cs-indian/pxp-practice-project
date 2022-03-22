package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetEndpointsAndOrganisationIdResponseModel extends IModel {
  
  public static String PROCESS_DEFINITION_INFO = "processDefinitionInfo";
  public static String ORGANISATION_ID         = "organisationId";
  
  public Map<String, Object> getProcessDefinitionInfo();
  
  public void setProcessDefinitionInfo(Map<String, Object> processDefinitionInfo);
  
  public String getOrganisationId();
  
  public void setOrganisationId(String organisationId);
}
