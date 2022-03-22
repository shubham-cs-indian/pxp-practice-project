package com.cs.core.config.interactor.entity.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.entity.user.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public abstract class AbstractRole implements IRole {
  
  private static final long                serialVersionUID  = 1L;
  
  protected String                         id;
  protected String                         label;
  protected String                         description;
  protected String                         tooltip;
  protected Boolean                        isMandatory       = false;
  protected Boolean                        isStandard;
  protected String                         placeholder;
  protected String                         icon;
  protected String                         iconkey;
  protected String                         type              = this.getClass()
      .getName();
  protected Boolean                        isMultiselect;
  protected Map<String, IGlobalPermission> globalPermission;
  protected List<? extends IUser>          users;
  protected Long                           versionId         = 0l;
  protected Long                           versionTimestamp  = 0l;
  protected String                         lastModifiedBy;
  protected List<String>                   entities;
  protected Boolean                        isSettingAllowed;
  protected List<String>                   endpoints;
  protected String                         code;
  protected List<String>                   physicalCatalogs;
  protected List<String>                   portals;
  protected List<String>                   availableTaxonomies;
  protected List<String>                   targetKlasses;
  protected List<String>                   targetTaxonomies;
  protected Boolean                        isAdmin           = false;
  protected String                         roleType;
  protected List<String>                   kpis;
  protected List<String>                   systems;
  protected String                         landingScreen     = "dashboard";
  protected Boolean                        isDashboardEnable = true;
  protected Boolean                        isBackgroundRole  = false;
  protected Boolean						             isReadOnly  = false;
  
  @Override
  public String getLandingScreen()
  {
    return landingScreen;
  }
  
  @Override
  public void setLandingScreen(String landingScreen)
  {
    this.landingScreen = landingScreen;
  }
  
  @Override
  public Boolean getIsDashboardEnable()
  {
    return isDashboardEnable;
  }
  
  @Override
  public void setIsDashboardEnable(Boolean isDashboardEnable)
  {
    this.isDashboardEnable = isDashboardEnable;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
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
  public List<String> getEntities()
  {
    if (entities == null) {
      entities = new ArrayList<>();
    }
    return entities;
  }
  
  @Override
  public void setEntities(List<String> entities)
  {
    this.entities = entities;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public String getDescription()
  {
    return this.description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  @Override
  public String getTooltip()
  {
    return this.tooltip;
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    this.tooltip = tooltip;
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    return this.isMandatory;
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    this.isMandatory = isMandatory;
  }
  
  @Override
  public String getPlaceholder()
  {
    return this.placeholder;
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    this.placeholder = placeholder;
  }
  
  @Override
  public String getLabel()
  {
    return this.label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getIcon()
  {
    return this.icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return this.iconkey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconkey = iconKey;
  }
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getType()
  {
    return this.type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public Boolean getIsMultiselect()
  {
    return this.isMultiselect;
  }
  
  @Override
  public void setIsMultiselect(Boolean isMultiselect)
  {
    this.isMultiselect = isMultiselect;
  }
  
  @Override
  public Map<String, IGlobalPermission> getGlobalPermission()
  {
    if (globalPermission == null) {
      globalPermission = new HashMap<String, IGlobalPermission>();
    }
    return globalPermission;
  }
  
  @JsonDeserialize(contentAs = GlobalPermission.class)
  @Override
  public void setGlobalPermission(Map<String, IGlobalPermission> globalPermission)
  {
    this.globalPermission = globalPermission;
  }
  
  @JsonDeserialize(contentAs = User.class)
  @Override
  public List<? extends IUser> getUsers()
  {
    if (users == null) {
      users = new ArrayList<IUser>();
    }
    return users;
  }
  
  @JsonDeserialize(contentAs = User.class)
  @Override
  public void setUsers(List<? extends IUser> users)
  {
    this.users = users;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return this.isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  @Override
  public Boolean getIsSettingAllowed()
  {
    return isSettingAllowed;
  }
  
  @Override
  public void setIsSettingAllowed(Boolean isSettingAllowed)
  {
    this.isSettingAllowed = isSettingAllowed;
  }
  
  @Override
  public List<String> getPhysicalCatalogs()
  {
    if (physicalCatalogs == null) {
      physicalCatalogs = new ArrayList<String>();
    }
    return physicalCatalogs;
  }
  
  @Override
  public void setPhysicalCatalogs(List<String> physicalCatalogs)
  {
    this.physicalCatalogs = physicalCatalogs;
  }
  
  @Override
  public List<String> getPortals()
  {
    if (portals == null) {
      portals = new ArrayList<String>();
    }
    return portals;
  }
  
  @Override
  public void setPortals(List<String> portals)
  {
    this.portals = portals;
  }
  
  @Override
  public List<String> getTargetKlasses()
  {
    if (targetKlasses == null) {
      targetKlasses = new ArrayList<>();
    }
    return targetKlasses;
  }
  
  @Override
  public void setTargetKlasses(List<String> targetKlasses)
  {
    this.targetKlasses = targetKlasses;
  }
  
  @Override
  public List<String> getTargetTaxonomies()
  {
    if (targetTaxonomies == null) {
      targetTaxonomies = new ArrayList<>();
    }
    return targetTaxonomies;
  }
  
  @Override
  public void setTargetTaxonomies(List<String> targetTaxonomies)
  {
    this.targetTaxonomies = targetTaxonomies;
  }
  
  @Override
  public String getRoleType()
  {
    return roleType;
  }
  
  @Override
  public void setRoleType(String roleType)
  {
    this.roleType = roleType;
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
  public Boolean getIsBackgroundRole()
  {
    return isBackgroundRole;
  }
  
  @Override
  public void setIsBackgroundRole(Boolean isBackgroundRole)
  {
    this.isBackgroundRole = isBackgroundRole;
  }
  
  @Override
  public Boolean getIsReadOnly()
  {
    return isReadOnly;
  }
  
  @Override
  public void setIsReadOnly(Boolean isReadOnly)
  {
    this.isReadOnly = isReadOnly;
  }
  
}
