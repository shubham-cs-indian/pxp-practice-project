package com.cs.core.config.interactor.entity.user;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IUser extends IConfigMasterEntity {
  
  public static final String FIRST_NAME              = "firstName";
  public static final String LAST_NAME               = "lastName";
  public static final String GENDER                  = "gender";
  public static final String EMAIL                   = "email";
  public static final String CONTACT                 = "contact";
  public static final String BIRTH_DATE              = "birthDate";
  public static final String USER_NAME               = "userName";
  public static final String PASSWORD                = "password";
  public static final String IS_STANDARD             = "isStandard";
  public static final String USER_IID                = "userIID";
  public static final String IS_BACKGROUND_USER      = "isBackgroundUser";
  

  public String getFirstName();
  
  public void setFirstName(String firstName);
  
  public String getLastName();
  
  public void setLastName(String lastName);
  
  public String getGender();
  
  public void setGender(String gender);
  
  public String getEmail();
  
  public void setEmail(String email);
  
  public String getContact();
  
  public void setContact(String contact);
  
  public String getBirthDate();
  
  public void setBirthDate(String birthDate);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public String getUserName();
  
  public void setUserName(String userName);
  
  public String getPassword();
  
  public void setPassword(String password);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);

  public long getUserIID();

  public void setUserIID(long userIID);
  
  public Boolean getIsBackgroundUser();
  
  public void setIsBackgroundUser(Boolean isBackgroundUser);
  
}
