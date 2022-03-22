package com.cs.core.config.interactor.model.user;

import com.cs.core.config.interactor.entity.module.IScreenModuleMapping;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

import java.util.List;

public interface IUserInformationModel extends IConfigModel {
  
  public static final String ROLE_TYPE               = "roleType";
  public static final String ROLE_ID                 = "roleId";
  public static final String IS_READ_ONLY            = "isReadOnly";
  public static final String PREFERRED_DATA_LANGUAGE = "preferredDataLanguage";
  public static final String PREFERRED_UI_LANGUAGE   = "preferredUILanguage";
  
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
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public IScreenModuleMapping getScreenModuleMapping();
  
  public void setScreenModuleMapping(IScreenModuleMapping screenModuleMapping);
  
  public Boolean getIsSettingAllowed();
  
  public void setIsSettingAllowed(Boolean isSettingAllowed);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public List<String> getAllowedPhysicalCatalogIds();
  
  public void setAllowedPhysicalCatalogIds(List<String> allowedPhysicalCatalogIds);
  
  public List<String> getAllowedPortalIds();
  
  public void setAllowedPortalIds(List<String> allowedPortalIds);
  
  public String getRoleType();
  
  public void setRoleType(String roleType);
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public Boolean getIsReadOnly();
  
  public void setIsReadOnly(Boolean isReadOnly);
  
  public long getUserIID();
  
  public void setUserIID(long iid);
  
  public String getPreferredDataLanguage();
 
  public void setPreferredDataLanguage(String preferredDataLanguage);
  
  public String getPreferredUILanguage();
 
  public void setPreferredUILanguage(String preferredUILanguage);
}
