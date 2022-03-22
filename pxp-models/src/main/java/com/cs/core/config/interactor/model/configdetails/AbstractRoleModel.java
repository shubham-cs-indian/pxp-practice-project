package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.role.IRoleModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractRoleModel implements IRoleModel {
  
  private static final long serialVersionUID = 1L;
  
  protected IRole           role;
  protected List<String>    endpoints;
  protected List<String>    kpis;
  protected List<String>    systems;
  
  public AbstractRoleModel(IRole role)
  {
    this.role = role;
  }
  
  @Override
  public List<String> getSystems()
  {
    if (systems == null) {
      return new ArrayList<>();
    }
    return systems;
  }
  
  @Override
  public void setSystems(List<String> systemIds)
  {
    this.systems = systemIds;
  }
  
  @Override
  public List<String> getEndpoints()
  {
    if (endpoints == null) {
      return new ArrayList<>();
    }
    return endpoints;
  }
  
  @Override
  public void setEndpoints(List<String> endpoints)
  {
    this.endpoints = endpoints;
  }
  
  @Override
  public List<String> getKpis()
  {
    if (kpis == null) {
      return new ArrayList<>();
    }
    return kpis;
  }
  
  @Override
  public void setKpis(List<String> kpis)
  {
    this.kpis = kpis;
  }
  
  @Override
  public Boolean getIsSettingAllowed()
  {
    return role.getIsSettingAllowed();
  }
  
  @Override
  public void setIsSettingAllowed(Boolean isSettingAllowed)
  {
    role.setIsSettingAllowed(isSettingAllowed);
  }
  
  @JsonIgnore
  @Override
  public IEntity getEntity()
  {
    return role;
  }
  
  @Override
  public String getId()
  {
    return role.getId();
  }
  
  @Override
  public void setId(String id)
  {
    role.setId(id);
  }
  
  @Override
  public String getDescription()
  {
    return role.getDescription();
  }
  
  @Override
  public void setDescription(String description)
  {
    role.setDescription(description);
  }
  
  @Override
  public String getTooltip()
  {
    return role.getTooltip();
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    role.setTooltip(tooltip);
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    return role.getIsMandatory();
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    role.setIsMandatory(isMandatory);
  }
  
  @Override
  public String getPlaceholder()
  {
    return role.getPlaceholder();
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    role.setPlaceholder(placeholder);
  }
  
  @Override
  public String getLabel()
  {
    return role.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    role.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return role.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    role.setIcon(icon);
  }
  
  @Override
  public String getIconKey()
  {
    return role.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    role.setIconKey(iconKey);
  }
  
  @Override
  public String getType()
  {
    return this.role.getType();
  }
  
  @Override
  public void setType(String type)
  {
    this.role.setType(type);
  }
  
  @Override
  public Boolean getIsReadOnly()
  {
    return role.getIsReadOnly();
  }
  
  @Override
  public void setIsReadOnly(Boolean isReadOnly)
  {
    role.setIsReadOnly(isReadOnly);
  }
  
  @Override
  public Long getVersionId()
  {
    return role.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    role.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return role.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    role.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return role.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    role.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return role.getIsStandard();
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    role.setIsStandard(isStandard);
  }
  
  @Override
  public List<String> getEntities()
  {
    return role.getEntities();
  }
  
  @Override
  public void setEntities(List<String> entities)
  {
    role.setEntities(entities);
  }
  
  @Override
  public List<String> getPhysicalCatalogs()
  {
    return role.getPhysicalCatalogs();
  }
  
  @Override
  public void setPhysicalCatalogs(List<String> availableTaxonomies)
  {
    role.setPhysicalCatalogs(availableTaxonomies);
  }
  
  @Override
  public List<String> getPortals()
  {
    return role.getPortals();
  }
  
  @Override
  public void setPortals(List<String> portals)
  {
    role.setPortals(portals);
  }
  
  public List<String> getTargetKlasses()
  {
    return role.getTargetKlasses();
  }
  
  @Override
  public void setTargetKlasses(List<String> tarhetKlasses)
  {
    role.setTargetKlasses(tarhetKlasses);
  }
  
  @Override
  public List<String> getTargetTaxonomies()
  {
    return role.getTargetTaxonomies();
  }
  
  @Override
  public void setTargetTaxonomies(List<String> targetTaxonomies)
  {
    role.setTargetTaxonomies(targetTaxonomies);
  }
  
  @Override
  public String getRoleType()
  {
    return role.getRoleType();
  }
  
  @Override
  public void setRoleType(String roleType)
  {
    role.setRoleType(roleType);
  }
  
  @Override
  public String toString()
  {
    return "\nID : " + getId() + " --- Name : " + getLabel();
  }
}
