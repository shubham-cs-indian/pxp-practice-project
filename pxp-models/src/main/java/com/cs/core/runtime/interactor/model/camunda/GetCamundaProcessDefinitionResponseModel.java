package com.cs.core.runtime.interactor.model.camunda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetCamundaProcessDefinitionResponseModel
    implements IGetCamundaProcessDefinitionResponseModel {

  private static final long     serialVersionUID       = 1L;
  protected String              id;
  protected String              label;
  protected List<String>        physicalCatalogIds     = new ArrayList<>();
  protected Map<String, Object> processDefinition      = new HashMap<>();
  protected List<String>        mappingIds             = new ArrayList<>();
  protected List<String>        partnerAuthorizationIds = new ArrayList<>();
  protected List<String>        organizationsIds        = new ArrayList<>();
  
  @Override
  public String getId()
  {
    return id;
  }

  @Override
  public void setId(String id)
  {
    this.id = id;
  }

  @Override
  public Map<String, Object> getProcessDefinition() {
    return this.processDefinition;
  }
  
  @Override
  public void setProcessDefinition(Map<String, Object> processDefinition) {
    this.processDefinition = processDefinition;
  }

  @Override
  public String getLabel()
  {
    return label;
  }

  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public List<String> getPhysicalCatalogIds()
  {
    return physicalCatalogIds;
  }

  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    this.physicalCatalogIds = physicalCatalogIds;
  }

  @Override
  public List<String> getMappingIds()
  {
    return mappingIds;
  }

  @Override
  public void setMappingIds(List<String> mappingIds)
  {
    this.mappingIds = mappingIds;
  }

  @Override
  public List<String> getPartnerAuthorizationIds()
  {
    return partnerAuthorizationIds;
  }

  @Override
  public void setPartnerAuthorizationIds(List<String> partnerAuthorizationIds)
  {
    this.partnerAuthorizationIds = partnerAuthorizationIds;
  }
  
  @Override
  public List<String> getOrganizationsIds()
  {
    return organizationsIds;
  }

  @Override
  public void setOrganizationsIds(List<String> organizationsIds)
  {
    this.organizationsIds = organizationsIds;
  }
}
