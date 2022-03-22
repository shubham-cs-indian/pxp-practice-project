package com.cs.core.config.interactor.model.user;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.entity.user.User;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

public class UserModel extends ConfigResponseWithAuditLogModel implements IUserModel {
  
  private static final long serialVersionUID = 1L;
  
  protected IUser           entity;
  protected String          roleId;
  protected String          organizationName;
  protected String          organizationType;
  protected String          preferredDataLanguage;
  protected String          preferredUILanguage;
  
  
  public UserModel()
  {
    this.entity = new User();
  }
  
  public UserModel(IUser user)
  {
    this.entity = user;
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
    return this.entity.getContact();
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
  public String getIconKey()
  {
    return this.entity.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.entity.setIconKey(iconKey);
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
  public String getUserName()
  {
    return entity.getUserName();
  }
  
  @Override
  public void setUserName(String userName)
  {
    entity.setUserName(userName);
  }
  
  @Override
  public String getPassword()
  {
    return entity.getPassword();
  }
  
  @Override
  public void setPassword(String password)
  {
    entity.setPassword(password);
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
  public long getUserIID() {
    return entity.getUserIID();
  }

  @Override
  public void setUserIID(long userIID) {
    entity.setUserIID(userIID);
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
  public Boolean getIsBackgroundUser()
  {
    return entity.getIsBackgroundUser();
  }
  
  @Override
  public void setIsBackgroundUser(Boolean isBackground)
  {
    entity.setIsBackgroundUser(isBackground);
  }

  @Override
  public String getOrganizationName()
  {
    return organizationName;
  }

  @Override
  public void setOrganizationName(String organizationName)
  {
    this.organizationName = organizationName;
  }

  @Override
  public String getOrganizationType()
  {
    return organizationType;
  }

  @Override
  public void setOrganizationType(String organizationType)
  {
    this.organizationType = organizationType;
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
