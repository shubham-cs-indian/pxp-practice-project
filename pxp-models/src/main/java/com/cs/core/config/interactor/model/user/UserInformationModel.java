package com.cs.core.config.interactor.model.user;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.module.IScreenModuleMapping;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInformationModel implements IUserInformationModel {
  
  private static final long serialVersionUID = -3813385873573523855L;
  
  protected IUser           user;
  protected String          preferredDataLanguage;
  protected String          preferredUILanguage;
  
  public UserInformationModel()
  {
    this.user = new User();
  }
  
  public UserInformationModel(IUser user)
  {
    this.user = user;
  }
  
  @Override
  public IEntity getEntity()
  {
    return this.user;
  }
  
  @Override
  public String getCode()
  {
    return this.user.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    this.user.setCode(code);
  }
  
  @Override
  public String getId()
  {
    return this.user.getId();
  }
  
  @Override
  public void setId(String id)
  {
    this.user.setId(id);
  }
  
  @Override
  public String getFirstName()
  {
    return this.user.getFirstName();
  }
  
  @Override
  public void setFirstName(String firstName)
  {
    this.user.setFirstName(firstName);
  }
  
  @Override
  public String getLastName()
  {
    return this.user.getLastName();
  }
  
  @Override
  public void setLastName(String lastName)
  {
    this.user.setLastName(lastName);
  }
  
  @Override
  public String getGender()
  {
    return this.user.getGender();
  }
  
  @Override
  public void setGender(String gender)
  {
    this.user.setGender(gender);
  }
  
  @Override
  public String getEmail()
  {
    return this.user.getEmail();
  }
  
  @Override
  public void setEmail(String email)
  {
    this.user.setEmail(email);
  }
  
  @Override
  public String getContact()
  {
    return this.user.getContact();
  }
  
  @Override
  public void setContact(String contact)
  {
    this.user.setContact(contact);
  }
  
  @Override
  public String getBirthDate()
  {
    return this.user.getBirthDate();
  }
  
  @Override
  public void setBirthDate(String birthDate)
  {
    this.user.setBirthDate(birthDate);
  }
  
  @Override
  public String getLabel()
  {
    return this.user.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    this.user.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return this.user.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.user.setIcon(icon);
  }
  
  public String getType()
  {
    return user.getType();
  }
  
  public void setType(String type)
  {
    user.setType(type);
  }
  
  @Override
  public String getUserName()
  {
    return user.getUserName();
  }
  
  @Override
  public void setUserName(String userName)
  {
    user.setUserName(userName);
  }
  
  @Override
  public Long getVersionId()
  {
    return user.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    user.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return user.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    user.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return user.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    user.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public long getUserIID()
  {
    return this.user.getUserIID();
  }

  @Override
  public void setUserIID(long iid)
  {
    this.user.setUserIID(iid);
  }
  
  /**
   * ************************************** ignored properties
   * *************************************************
   */
  @Override
  @JsonIgnore
  public IScreenModuleMapping getScreenModuleMapping()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setScreenModuleMapping(IScreenModuleMapping screenModuleMapping)
  {
  }
  
  @Override
  @JsonIgnore
  public Boolean getIsSettingAllowed()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setIsSettingAllowed(Boolean isSettingAllowed)
  {
  }
  
  @Override
  @JsonIgnore
  public String getOrganizationId()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setOrganizationId(String organizationId)
  {
  }
  
  @Override
  @JsonIgnore
  public List<String> getAllowedPhysicalCatalogIds()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setAllowedPhysicalCatalogIds(List<String> allowedPhysicalCatalogIds)
  {
  }
  
  @Override
  @JsonIgnore
  public List<String> getAllowedPortalIds()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setAllowedPortalIds(List<String> allowedPortalIds)
  {
  }
  
  @Override
  public String getRoleType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setRoleType(String roleType)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getRoleId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setRoleId(String roleId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Boolean getIsReadOnly()
  {
    return null;
  }
  
  @Override
  public void setIsReadOnly(Boolean isReadOnly)
  {
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
