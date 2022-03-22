package com.cs.core.config.interactor.model.user;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetCurrentUserModel implements IGetCurrentUserModel {
  
  private static final long serialVersionUID = 1L;
  
  protected IUser           user;
  protected List<String>    entities;
  protected Boolean         isSettingAllowed;
  protected String          organizationId;
  protected List<String>    allowedPhysicalCatalogIds;
  protected List<String>    allowedPortalIds;
  protected String          roleType;
  protected String          landingScreen;
  protected Boolean         isDashboardEnable;
  protected String          roleId;
  protected Boolean 	     	isReadOnly = false;
  protected String          preferredDataLanguage;
  protected String          preferredUILanguage;
  
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
    return user.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    this.user.setCode(code);
  }
  
  @Override
  public IUser getUser()
  {
    return user;
  }
  
  @Override
  @JsonDeserialize(as = User.class)
  public void setUser(IUser user)
  {
    this.user = user;
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
  public String getOrganizationId()
  {
    return organizationId;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
  
  @Override
  public List<String> getAllowedPhysicalCatalogIds()
  {
    return allowedPhysicalCatalogIds;
  }
  
  @Override
  public void setAllowedPhysicalCatalogIds(List<String> allowedPhysicalCatalogIds)
  {
    this.allowedPhysicalCatalogIds = allowedPhysicalCatalogIds;
  }
  
  @Override
  public List<String> getAllowedPortalIds()
  {
    return allowedPortalIds;
  }
  
  @Override
  public void setAllowedPortalIds(List<String> allowedPortalIds)
  {
    this.allowedPortalIds = allowedPortalIds;
  }
  
  /**
   * ********************************************** ignored properties
   * ******************************************************************
   */
  @Override
  @JsonIgnore
  public IEntity getEntity()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public String getId()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setId(String id)
  {
  }
  
  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionId(Long versionId)
  {
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
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
  public String getRoleId()
  {
    return roleId;
  }
  
  @Override
  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
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
  
  @Override
  public String getPreferredDataLanguage()
  {
    return preferredDataLanguage;
  }
  
  @Override
  public void setPreferredDataLanguage(String preferredDataLanguage)
  {
    this.preferredDataLanguage = preferredDataLanguage;
  }
  
  @Override
  public String getPreferredUILanguage()
  {
    return preferredUILanguage;
  }
  
  @Override
  public void setPreferredUILanguage(String preferredUILanguage)
  {
    this.preferredUILanguage = preferredUILanguage;
  }
}
