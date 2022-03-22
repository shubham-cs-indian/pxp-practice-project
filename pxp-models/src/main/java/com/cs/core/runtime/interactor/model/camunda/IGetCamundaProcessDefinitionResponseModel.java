package com.cs.core.runtime.interactor.model.camunda;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetCamundaProcessDefinitionResponseModel extends IModel {
  
  public static final String ID                       = "id";
  public static final String LABEL                    = "label";
  public static final String PHYSICAL_CATALOG_IDS     = "physicalCatalogIds";
  public static final String PROCESS_DEFINITION       = "processDefinition";
  public static final String MAPPING_IDS              = "mappingIds";
  public static final String PARTNER_AUTHORIZATION_IDS = "partnerAuthorizationIds";
  public static final String ORGANIZATIONSIDS          = "organizationsIds";
  
  String getId();
  
  void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Map<String, Object> getProcessDefinition();
  
  public void setProcessDefinition(Map<String, Object> processDefinition);
  
  public List<String> getPhysicalCatalogIds();
  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
  
  public List<String> getMappingIds();
  
  public void setMappingIds(List<String> mappingIds);
  
  public List<String> getPartnerAuthorizationIds();

  public void setPartnerAuthorizationIds(List<String> partnerAuthorizationIds);
  
  public List<String> getOrganizationsIds();
  
  public void setOrganizationsIds(List<String> organizationsIds);
}
