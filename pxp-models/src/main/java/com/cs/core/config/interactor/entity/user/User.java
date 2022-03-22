package com.cs.core.config.interactor.entity.user;

import com.cs.core.runtime.interactor.entity.role.AbstractRoleCandidate;

public class User extends AbstractRoleCandidate implements IUser {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  
  protected String          label;
  
  protected String          icon;
  
  protected String          iconKey;
  
  protected String          firstName;
  
  protected String          lastName;
  
  protected String          gender;
  
  protected String          contact;
  
  protected String          birthDate;
  
  protected String          userName;
  
  protected String          password;
  
  protected Long            versionId;
  
  protected Long            versionTimestamp;
  
  protected String          lastModifiedBy;
  
  protected Boolean         isStandard;
  
  protected String          code;

  protected long            userIID;
  
  protected Boolean         isBackgroundUser = false;
  
  protected String          preferredDataLanguage;
  
  protected String          preferredUILanguage;
  
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
    return this.iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
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
  public String getFirstName()
  {
    return this.firstName;
  }
  
  @Override
  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }
  
  @Override
  public String getLastName()
  {
    return this.lastName;
  }
  
  @Override
  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }
  
  @Override
  public String getGender()
  {
    return this.gender;
  }
  
  @Override
  public void setGender(String gender)
  {
    this.gender = gender;
  }
  
  @Override
  public String getContact()
  {
    return this.contact;
  }
  
  @Override
  public void setContact(String contact)
  {
    this.contact = contact;
  }
  
  @Override
  public String getBirthDate()
  {
    return this.birthDate;
  }
  
  @Override
  public void setBirthDate(String birthDate)
  {
    this.birthDate = birthDate;
  }
  
  @Override
  public String getUserName()
  {
    return userName;
  }
  
  @Override
  public void setUserName(String userName)
  {
    this.userName = userName;
  }
  
  @Override
  public String getPassword()
  {
    return password;
  }
  
  @Override
  public void setPassword(String password)
  {
    this.password = password;
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
  public boolean equals(Object obj)
  {
    boolean isEqual = false;
    User that = (User) obj;
    
    if (this.id.equals(that.getId()) && this.label.equals(that.getLabel())
        && this.firstName.equals(that.getFirstName()) && this.lastName.equals(that.getLastName())
        && this.birthDate.equals(that.getBirthDate()) && this.userName.equals(that.getUserName())
        && this.contact.equals(that.getContact()) && this.email.equals(that.getEmail())
        && this.gender.equals(that.getGender()) && this.password.equals(that.getPassword())) {
      isEqual = true;
    }
    
    return isEqual;
  }
  
  @Override
  public int hashCode()
  {
    int hashCode = super.hashCode();
    if (this.id != null) {
      hashCode += this.id.hashCode();
    }
    
    return hashCode;
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }

  @Override
  public long getUserIID() {
    return userIID;
  }

  @Override
  public void setUserIID(long userIID) {
    this.userIID = userIID;
  }
  
  @Override
  public Boolean getIsBackgroundUser()
  {
    return isBackgroundUser;
  }
  
  @Override
  public void setIsBackgroundUser(Boolean isBackgroundUser)
  {
    this.isBackgroundUser = isBackgroundUser;
  }

}
