package com.cs.core.config.interactor.model.organization;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.organization.IOrganization;
import com.cs.core.config.interactor.entity.organization.Organization;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class OrganizationModel implements IOrganizationModel {
  
  private static final long serialVersionUID    = 1L;
  protected IOrganization   organization;
  protected String          originalInstanceId;         // if creating supplier
                                                        // instance from
                                                        // onboarding
  protected Boolean         isOnboardingRequest = false;
  
  public OrganizationModel()
  {
    organization = new Organization();
  }
  
  public OrganizationModel(IOrganization organization)
  {
    this.organization = organization;
  }
  
  @Override
  @JsonIgnore
  public IEntity getEntity()
  {
    return organization;
  }
  
  @Override
  public String getId()
  {
    return organization.getId();
  }
  
  @Override
  public void setId(String id)
  {
    organization.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<String> getPhysicalCatalogs()
  {
    return organization.getPhysicalCatalogs();
  }
  
  @Override
  public void setPhysicalCatalogs(List<String> physicalCatelogs)
  {
    organization.setPhysicalCatalogs(physicalCatelogs);
  }
  
  @Override
  public List<String> getPortals()
  {
    return organization.getPortals();
  }
  
  @Override
  public void setPortals(List<String> portals)
  {
    organization.setPortals(portals);
  }
  
  @Override
  public List<String> getRoleIds()
  {
    return organization.getRoleIds();
  }
  
  @Override
  public void setRoleIds(List<String> roleIds)
  {
    organization.setRoleIds(roleIds);
  }
  
  @Override
  public String getLabel()
  {
    return organization.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    organization.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return organization.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    organization.setIcon(icon);
  }

  @Override
  public String getIconKey()
  {
    return organization.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    organization.setIconKey(iconKey);
  }
  
  
  @Override
  public String getType()
  {
    return organization.getType();
  }
  
  @Override
  public void setType(String type)
  {
    organization.setType(type);
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    return organization.getTaxonomyIds();
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    organization.setTaxonomyIds(taxonomyIds);
  }
  
  @Override
  public List<String> getEndpointIds()
  {
    return organization.getEndpointIds();
  }
  
  @Override
  public void setEndpointIds(List<String> endpointIds)
  {
    organization.setEndpointIds(endpointIds);
  }
  
  @Override
  public List<String> getKlassIds()
  {
    return organization.getKlassIds();
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    organization.setKlassIds(klassIds);
  }
  
  @Override
  public String getCode()
  {
    return organization.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    organization.setCode(code);
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return organization.getIsStandard();
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    organization.setIsStandard(isStandard);
  }
  
  @Override
  public List<String> getSystems()
  {
    return organization.getSystems();
  }
  
  @Override
  public void setSystems(List<String> systems)
  {
    organization.setSystems(systems);
  }
  
  @Override
  public String getOriginalInstanceId()
  {
    return originalInstanceId;
  }
  
  @Override
  public void setOriginalInstanceId(String originalInstanceId)
  {
    this.originalInstanceId = originalInstanceId;
  }
  
  @Override
  public Boolean getIsOnboardingRequest()
  {
    return isOnboardingRequest;
  }
  
  @Override
  public void setIsOnboardingRequest(Boolean isOnboardingRequest)
  {
    this.isOnboardingRequest = isOnboardingRequest;
  }
}
