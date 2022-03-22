package com.cs.core.config.interactor.model.user;

import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

import java.util.List;

public interface IGetCurrentUserModel extends IConfigModel {
  
  public static final String USER                         = "user";
  public static final String ENTITIES                     = "entities";
  public static final String IS_SETTING_ALLOWED           = "isSettingAllowed";
  public static final String ORGANIZATION_ID              = "organizationId";
  public static final String ALLOWED_PHYSICAL_CATALOG_IDS = "allowedPhysicalCatalogIds";
  public static final String ALLOWED_PORTAL_IDS           = "allowedPortalIds";
  public static final String ROLE_TYPE                    = "roleType";
  public static final String LANDING_SCREEN               = "landingScreen";
  public static final String IS_DASHBOARD_ENABLE          = "isDashboardEnable";
  public static final String IS_READ_ONLY                 = "isReadOnly";
  public static final String PREFERRED_DATA_LANGUAGE      = "preferredDataLanguage";
  public static final String PREFERRED_UI_LANGUAGE        = "preferredUILanguage";
  
  public IUser getUser();
  
  public void setUser(IUser user);
  
  public List<String> getEntities();
  
  public void setEntities(List<String> entities);
  
  public Boolean getIsSettingAllowed();
  
  public void setIsSettingAllowed(Boolean isSettinAllowed);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public List<String> getAllowedPhysicalCatalogIds();
  
  public void setAllowedPhysicalCatalogIds(List<String> allowedPhysicalCatalogIds);
  
  public List<String> getAllowedPortalIds();
  
  public void setAllowedPortalIds(List<String> allowedPortalIds);
  
  public String getRoleType();
  
  public void setRoleType(String roleType);
  
  public String getLandingScreen();
  
  public void setLandingScreen(String landingScreen);
  
  public Boolean getIsDashboardEnable();
  
  public void setIsDashboardEnable(Boolean isDashboardVisible);
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public Boolean getIsReadOnly();
  
  public void setIsReadOnly(Boolean isReadOnly);
  
  public String getPreferredDataLanguage();
  
  public void setPreferredDataLanguage(String preferredDataLanguage);
  
  public String getPreferredUILanguage();
 
  public void setPreferredUILanguage(String preferredUILanguage);
}
