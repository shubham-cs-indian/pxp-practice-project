package com.cs.core.config.interactor.model.role;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.role.Role;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.entity.user.User;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

// @JsonIgnoreProperties("klassType")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = RoleModel.class,
    include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
public class RoleModel implements IRoleModel {
  
  private static final long serialVersionUID = 1L;
  
  protected IRole           entity;
  
  public RoleModel()
  {
    entity = new Role();
  }
  
  public RoleModel(IRole role)
  {
    entity = role;
  }
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
  }
  
  @Override
  public IEntity getEntity()
  {
    return entity;
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public String getDescription()
  {
    return entity.getDescription();
  }
  
  @Override
  public void setDescription(String description)
  {
    entity.setDescription(description);
  }
  
  @Override
  public String getTooltip()
  {
    return entity.getTooltip();
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    entity.setTooltip(tooltip);
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    return entity.getIsMandatory();
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    entity.setIsMandatory(isMandatory);
  }
  
  @Override
  public String getPlaceholder()
  {
    return entity.getPlaceholder();
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    entity.setPlaceholder(placeholder);
  }
  
  @Override
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    entity.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return entity.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    entity.setIcon(icon);
  }
  
  @Override
  public String getIconKey()
  {
    return entity.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    entity.setIconKey(iconKey);
  }
  
  @Override
  public String getType()
  {
    return entity.getType();
  }
  
  @Override
  public void setType(String type)
  {
    entity.setType(type);
  }
  
  @Override
  public Boolean getIsMultiselect()
  {
    return this.entity.getIsMultiselect();
  }
  
  @Override
  public void setIsMultiselect(Boolean isMultiselect)
  {
    this.entity.setIsMultiselect(isMultiselect);
  }
  
  @Override
  public Map<String, IGlobalPermission> getGlobalPermission()
  {
    return entity.getGlobalPermission();
  }
  
  @JsonDeserialize(contentAs = GlobalPermission.class)
  @Override
  public void setGlobalPermission(Map<String, IGlobalPermission> globalPermission)
  {
    entity.setGlobalPermission(globalPermission);
  }
  
  @JsonDeserialize(contentAs = User.class)
  @Override
  public List<? extends IUser> getUsers()
  {
    return entity.getUsers();
  }
  
  @JsonDeserialize(contentAs = User.class)
  @Override
  public void setUsers(List<? extends IUser> users)
  {
    entity.setUsers(users);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return entity.getIsStandard();
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    entity.setIsStandard(isStandard);
  }
  
  @Override
  public List<String> getEntities()
  {
    return entity.getEntities();
  }
  
  @Override
  public void setEntities(List<String> entities)
  {
    entity.setEntities(entities);
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public Boolean getIsSettingAllowed()
  {
    return entity.getIsSettingAllowed();
  }
  
  @Override
  public void setIsSettingAllowed(Boolean isSettingAllowed)
  {
    entity.setIsSettingAllowed(isSettingAllowed);
  }
  
  @Override
  public List<String> getEndpoints()
  {
    return entity.getEndpoints();
  }
  
  @Override
  public void setEndpoints(List<String> endpoints)
  {
    entity.setEndpoints(endpoints);
  }
  
  @Override
  public List<String> getPhysicalCatalogs()
  {
    return entity.getPhysicalCatalogs();
  }
  
  @Override
  public void setPhysicalCatalogs(List<String> availableTaxonomies)
  {
    entity.setPhysicalCatalogs(availableTaxonomies);
  }
  
  @Override
  public List<String> getPortals()
  {
    return entity.getPortals();
  }
  
  @Override
  public void setPortals(List<String> portals)
  {
    entity.setPortals(portals);
  }
  
  public List<String> getTargetKlasses()
  {
    return entity.getTargetKlasses();
  }
  
  @Override
  public void setTargetKlasses(List<String> tarhetKlasses)
  {
    entity.setTargetKlasses(tarhetKlasses);
  }
  
  @Override
  public List<String> getTargetTaxonomies()
  {
    return entity.getTargetTaxonomies();
  }
  
  @Override
  public void setTargetTaxonomies(List<String> targetTaxonomies)
  {
    entity.setTargetTaxonomies(targetTaxonomies);
  }
  
  @Override
  public String getRoleType()
  {
    return entity.getRoleType();
  }
  
  @Override
  public void setRoleType(String roleType)
  {
    entity.setRoleType(roleType);
  }
  
  @Override
  public List<String> getKpis()
  {
    return entity.getKpis();
  }
  
  @Override
  public void setKpis(List<String> kpis)
  {
    entity.setKpis(kpis);
  }
  
  @Override
  public List<String> getSystems()
  {
    return entity.getSystems();
  }
  
  @Override
  public void setSystems(List<String> systems)
  {
    entity.setSystems(systems);
  }
  
  @Override
  public String getLandingScreen()
  {
    return entity.getLandingScreen();
  }
  
  @Override
  public void setLandingScreen(String landingScreen)
  {
    entity.setLandingScreen(landingScreen);
  }
  
  @Override
  public Boolean getIsDashboardEnable()
  {
    return entity.getIsDashboardEnable();
  }
  
  @Override
  public void setIsDashboardEnable(Boolean isDashboardVisible)
  {
    entity.setIsDashboardEnable(isDashboardVisible);
  }

  @Override
  public Boolean getIsBackgroundRole()
  {
    return entity.getIsBackgroundRole();
  }

  @Override
  public void setIsBackgroundRole(Boolean isBackgroundRole)
  {
    entity.setIsBackgroundRole(isBackgroundRole);
  }
  
  @Override
  public Boolean getIsReadOnly()
  {
    return entity.getIsReadOnly();
  }
  
  @Override
  public void setIsReadOnly(Boolean isReadOnly)
  {
    entity.setIsReadOnly(isReadOnly);
  }
}
