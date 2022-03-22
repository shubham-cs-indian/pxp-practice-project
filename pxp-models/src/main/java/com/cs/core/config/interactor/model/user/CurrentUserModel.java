package com.cs.core.config.interactor.model.user;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.module.IScreenModuleMapping;
import com.cs.core.config.interactor.entity.module.ScreenModuleMapping;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.entity.user.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class CurrentUserModel implements IUserInformationModel {
  
  private static final long      serialVersionUID = 1L;
  
  protected IUser                entity;
  protected IScreenModuleMapping screenModuleMapping;
  protected Boolean              isSettingAllowed;
  protected String               organizationId;
  protected List<String>         physicalCatalogIds;
  protected List<String>         portalIds;
  protected String               roleType;
  protected int                  shouldMaintainArchives;
  protected String               roleId;
  protected Boolean              isReadOnly       = false;
  protected String               preferredDataLanguage;
  protected String               preferredUILanguage;
  
  public CurrentUserModel()
  {
    this.entity = new User();
  }
  
  public CurrentUserModel(IUser user)
  {
    this.entity = user;
  }
  
  @Override
  public String getCode()
  {
    return this.entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    this.entity.setCode(code);
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
  public IScreenModuleMapping getScreenModuleMapping()
  {
    return screenModuleMapping;
  }
  
  @Override
  @JsonDeserialize(contentAs = ScreenModuleMapping.class)
  public void setScreenModuleMapping(IScreenModuleMapping screenModuleMapping)
  {
    this.screenModuleMapping = screenModuleMapping;
  }
  
  @Override
  public IEntity getEntity()
  {
    return this.entity;
  }
  
  @Override
  public String getId()
  {
    return this.entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    this.entity.setId(id);
  }
  
  @Override
  public String getFirstName()
  {
    return this.entity.getFirstName();
  }
  
  @Override
  public void setFirstName(String firstName)
  {
    this.entity.setFirstName(firstName);
  }
  
  @Override
  public String getLastName()
  {
    return this.entity.getLastName();
  }
  
  @Override
  public void setLastName(String lastName)
  {
    this.entity.setLastName(lastName);
  }
  
  @Override
  public String getGender()
  {
    return this.entity.getGender();
  }
  
  @Override
  public void setGender(String gender)
  {
    this.entity.setGender(gender);
  }
  
  @Override
  public String getEmail()
  {
    return this.entity.getEmail();
  }
  
  @Override
  public void setEmail(String email)
  {
    this.entity.setEmail(email);
  }
  
  @Override
  public String getContact()
  {
    return entity.getContact();
  }
  
  @Override
  public void setContact(String contact)
  {
    this.entity.setContact(contact);
  }
  
  @Override
  public String getBirthDate()
  {
    return this.entity.getBirthDate();
  }
  
  @Override
  public void setBirthDate(String birthDate)
  {
    this.entity.setBirthDate(birthDate);
  }
  
  @Override
  public String getUserName()
  {
    return this.entity.getUserName();
  }
  
  @Override
  public void setUserName(String userName)
  {
    this.entity.setUserName(userName);
  }
  
  @Override
  public String getLabel()
  {
    return this.entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    this.entity.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return this.entity.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.entity.setIcon(icon);
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
    return physicalCatalogIds;
  }
  
  @Override
  public void setAllowedPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    this.physicalCatalogIds = physicalCatalogIds;
  }
  
  @Override
  public List<String> getAllowedPortalIds()
  {
    return portalIds;
  }
  
  @Override
  public void setAllowedPortalIds(List<String> portalIds)
  {
    this.portalIds = portalIds;
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
  public long getUserIID()
  {
    return this.entity.getUserIID();
  }

  @Override
  public void setUserIID(long iid)
  {
    this.entity.setUserIID(iid);
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
