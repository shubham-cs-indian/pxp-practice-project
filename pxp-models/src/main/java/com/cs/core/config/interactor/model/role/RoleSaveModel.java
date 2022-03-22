package com.cs.core.config.interactor.model.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.role.Role;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = RoleSaveModel.class,
    include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
public class RoleSaveModel implements IRoleSaveModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Role            entity;
  protected List<String>    addedUsers;
  protected List<String>    deletedUsers;
  protected List<String>    addedEndpoints;
  protected List<String>    deletedEndpoints;
  protected List<String>    addedTargetKlasses;
  protected List<String>    addedAvailableTaxonomies;
  protected List<String>    addedTargetTaxonomies;
  protected List<String>    deletedTargetKlasses;
  protected List<String>    deletedTargetTaxonomies;
  protected List<String>    addedKPIs;
  protected List<String>    deletedKPIs;
  protected List<String>    addedSystemIds;
  protected List<String>    deletedSystemIds;
  
  public RoleSaveModel()
  {
    entity = new Role();
  }
  
  public RoleSaveModel(Role role)
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
  public List<String> getAddedUsers()
  {
    if (addedUsers == null) {
      addedUsers = new ArrayList<String>();
    }
    return addedUsers;
  }
  
  @Override
  public void setAddedUsers(List<String> userIdList)
  {
    addedUsers = userIdList;
  }
  
  @Override
  public List<String> getDeletedUsers()
  {
    if (deletedUsers == null) {
      deletedUsers = new ArrayList<String>();
    }
    return deletedUsers;
  }
  
  @Override
  public void setDeletedUsers(List<String> userIdList)
  {
    deletedUsers = userIdList;
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
    return entity.getIsMultiselect();
  }
  
  @Override
  public void setIsMultiselect(Boolean isMultiselect)
  {
    entity.setIsMultiselect(isMultiselect);
  }
  
  @Override
  @JsonDeserialize(contentAs = GlobalPermission.class)
  public Map<String, IGlobalPermission> getGlobalPermission()
  {
    return entity.getGlobalPermission();
  }
  
  @Override
  @JsonDeserialize(contentAs = GlobalPermission.class)
  public void setGlobalPermission(Map<String, IGlobalPermission> globalPermission)
  {
    entity.setGlobalPermission(globalPermission);
  }
  
  @Override
  @JsonIgnore
  public List<? extends IUser> getUsers()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setUsers(List<? extends IUser> users)
  {
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
  public List<String> getAddedEndpoints()
  {
    return this.addedEndpoints;
  }
  
  @Override
  public void setAddedEndpoints(List<String> endpointIdList)
  {
    this.addedEndpoints = endpointIdList;
  }
  
  @Override
  public List<String> getSystems()
  {
    return entity.getSystems();
  }
  
  @Override
  public void setSystems(List<String> systemIds)
  {
    entity.setSystems(systemIds);
  }
  
  @Override
  public List<String> getDeletedEndpoints()
  {
    return this.deletedEndpoints;
  }
  
  @Override
  public void setDeletedEndpoints(List<String> endpointIdList)
  {
    this.deletedEndpoints = endpointIdList;
  }
  
  @Override
  public List<String> getAddedTargetKlasses()
  {
    if (addedTargetKlasses == null) {
      addedTargetKlasses = new ArrayList<>();
    }
    return addedTargetKlasses;
  }
  
  @Override
  public void setAddedTargetKlasses(List<String> addedTargetKlasses)
  {
    this.addedTargetKlasses = addedTargetKlasses;
  }
  
  @Override
  public List<String> getAddedTargetTaxonomies()
  {
    if (addedTargetTaxonomies == null) {
      addedTargetTaxonomies = new ArrayList<>();
    }
    return addedTargetTaxonomies;
  }
  
  @Override
  public void setAddedTargetTaxonomies(List<String> addedTargetTaxonomies)
  {
    this.addedTargetTaxonomies = addedTargetTaxonomies;
  }
  
  @Override
  public List<String> getDeletedTargetKlasses()
  {
    if (deletedTargetKlasses == null) {
      deletedTargetKlasses = new ArrayList<>();
    }
    return deletedTargetKlasses;
  }
  
  @Override
  public void setDeletedTargetKlasses(List<String> deletedTargetKlasses)
  {
    this.deletedTargetKlasses = deletedTargetKlasses;
  }
  
  @Override
  public List<String> getDeletedTargetTaxonomies()
  {
    if (deletedTargetTaxonomies == null) {
      deletedTargetTaxonomies = new ArrayList<>();
    }
    return deletedTargetTaxonomies;
  }
  
  @Override
  public void setDeletedTargetTaxonomies(List<String> deletedTargetTaxonomies)
  {
    this.deletedTargetTaxonomies = deletedTargetTaxonomies;
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
  public Boolean getIsReadOnly()
  {
    return entity.getIsReadOnly();
  }
  
  @Override
  public void setIsReadOnly(Boolean isReadOnly)
  {
    entity.setIsReadOnly(isReadOnly);
  }
  
  /**
   * **************************** Ignored Properties
   * ************************************
   */
  @JsonIgnore
  @Override
  public List<String> getTargetKlasses()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setTargetKlasses(List<String> tarhetKlasses)
  {
  }
  
  @JsonIgnore
  @Override
  public List<String> getTargetTaxonomies()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setTargetTaxonomies(List<String> targetTaxonomies)
  {
  }
  
  @Override
  public List<String> getAddedKPIs()
  {
    return addedKPIs;
  }
  
  @Override
  public void setddedKPIs(List<String> addedKPIs)
  {
    this.addedKPIs = addedKPIs;
  }
  
  @Override
  public List<String> getDeletedKPIs()
  {
    return deletedKPIs;
  }
  
  @Override
  public void setDeletedKPIs(List<String> deletedKPIs)
  {
    this.deletedKPIs = deletedKPIs;
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
  public List<String> getDeletedSystemIds()
  {
    if (deletedSystemIds == null) {
      return new ArrayList<>();
    }
    return deletedSystemIds;
  }
  
  @Override
  public void setDeletedSystemIds(List<String> deletedSystemIds)
  {
    this.deletedSystemIds = deletedSystemIds;
  }
  
  @Override
  public List<String> getAddedSystemIds()
  {
    if (addedSystemIds == null) {
      return new ArrayList<>();
    }
    return addedSystemIds;
  }
  
  @Override
  public void setaddedSystemIds(List<String> addedSystemIds)
  {
    this.addedSystemIds = addedSystemIds;
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
}
