package com.cs.di.config.model.onboardinguser;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.onboardinguser.IOnboardingUser;
import com.cs.core.config.interactor.entity.onboardinguser.OnboardingUser;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = OnboardingUserModel.class,
    include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
public class OnboardingUserModel implements IOnboardingUserModel {
  
  private static final long serialVersionUID = 1L;
  
  protected IOnboardingUser entity;
  
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
  
  public OnboardingUserModel()
  {
    this.entity = new OnboardingUser();
  }
  
  public OnboardingUserModel(IOnboardingUser supplier)
  {
    this.entity = supplier;
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
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
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
  public long getUserIID() {
    return entity.getUserIID();
  }

  @Override
  public void setUserIID(long userIID) {
    entity.setUserIID(userIID);
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
}
