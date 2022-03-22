package com.cs.core.config.interactor.entity.role;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterPropertyEntity;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.user.IUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
public interface IRole extends IConfigMasterPropertyEntity {
  
  public static final String IS_MULTISELECT      = "isMultiselect";
  public static final String GLOBAL_PERMISSION   = "globalPermission";
  public static final String USERS               = "users";
  public static final String ENTITIES            = "entities";
  public static final String IS_SETTING_ALLOWED  = "isSettingAllowed";
  public static final String ENDPOINTS           = "endpoints";
  public static final String PHYSICAL_CATALOGS   = "physicalCatalogs";
  public static final String PORTALS             = "portals";
  public static final String TARGET_KLASSES      = "targetKlasses";
  public static final String TARGET_TAXONOMIES   = "targetTaxonomies";
  public static final String ROLE_TYPE           = "roleType";
  public static final String KPIS                = "kpis";
  public static final String SYSTEMS             = "systems";
  public static final String LANDING_SCREEN      = "landingScreen";
  public static final String IS_DASHBOARD_ENABLE = "isDashboardEnable";
  public static final String IS_BACKGROUND_ROLE  = "isBackgroundRole";
  public static final String IS_READ_ONLY		     = "isReadOnly";
  
  public List<String> getEndpoints();
  
  public void setEndpoints(List<String> endpoints);
  
  public List<String> getKpis();
  
  public void setKpis(List<String> kpis);
  
  public Boolean getIsMultiselect();
  
  public void setIsMultiselect(Boolean isMultiselect);
  
  public Map<String, IGlobalPermission> getGlobalPermission();
  
  public void setGlobalPermission(Map<String, IGlobalPermission> globalPermission);
  
  public List<? extends IUser> getUsers();
  
  public void setUsers(List<? extends IUser> users);
  
  public List<String> getEntities();
  
  public void setEntities(List<String> entities);
  
  public Boolean getIsSettingAllowed();
  
  public void setIsSettingAllowed(Boolean isSettingAllowed);
  
  public List<String> getPhysicalCatalogs();
  
  public void setPhysicalCatalogs(List<String> availableTaxonomies);
  
  public List<String> getPortals();
  
  public void setPortals(List<String> portals);
  
  public List<String> getTargetKlasses();
  
  public void setTargetKlasses(List<String> tarhetKlasses);
  
  public List<String> getTargetTaxonomies();
  
  public void setTargetTaxonomies(List<String> targetTaxonomies);
  
  public String getRoleType();
  
  public void setRoleType(String roleType);
  
  public List<String> getSystems();
  
  public void setSystems(List<String> systems);
  
  public String getLandingScreen();
  
  public void setLandingScreen(String landingScreen);
  
  public Boolean getIsDashboardEnable();
  
  public void setIsDashboardEnable(Boolean isDashboardEnable);
  
  public Boolean getIsBackgroundRole();
  
  public void setIsBackgroundRole(Boolean isBackgroundRole);
  
  public Boolean getIsReadOnly();
  
  public void setIsReadOnly(Boolean isReadOnly);
}
