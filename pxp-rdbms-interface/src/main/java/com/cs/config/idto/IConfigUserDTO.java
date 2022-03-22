package com.cs.config.idto;

/**
 * User DTO from the configuration realm
 *
 * @author janak
 */
public interface IConfigUserDTO extends IConfigJSONDTO {
  
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
  
  public String getIcon();
  
  public String getUserName();
  
  public void setUserName(String userName);
  
  public String getPassword();
  
  public void setPassword(String password);

  public Boolean getIsStandard();
  
  public void setIsStandard(boolean isStandard);

  public Boolean getIsBackgroundUser();
  
  public void setIsBackgroundUser(boolean isBackgroundUser);
  
  public Boolean getIsEmailLog();
  
  public void setIsEmailLog(boolean isEmailLog);

  public long getUserIID();
  
  public void setUserIID(long userIID);
  
  public void setCode(String code);
  
  public String getCode();
  
  public void setIcon(String icon);
  
  public String getPreferredDataLanguage();
  
  public void setPreferredDataLanguage(String preferredDataLanguage);
  
  public String getPreferredUILanguage();
  
  public void setPreferredUILanguage(String preferredUILanguage);
}
