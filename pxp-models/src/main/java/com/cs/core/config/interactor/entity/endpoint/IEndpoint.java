package com.cs.core.config.interactor.entity.endpoint;

import java.util.List;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IEndpoint extends IConfigEntity {
  
  public static final String LABEL                      = "label";
  public static final String INDEX_NAME                 = "indexName";
  public static final String IS_RUNTIME_MAPPING_ENABLED = "isRuntimeMappingEnabled";
  public static final String ENDPOINT_TYPE              = "endpointType";
  public static final String SYSTEM_ID                  = "systemId";
  public static final String DASHBOARD_TAB_ID           = "dashboardTabId";
  public static final String PHYSICAL_CATALOGS          = "physicalCatalogs";
  public static final String ICON_KEY                   = "iconKey";
  public static final String ICON                       = "icon";

  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getIndexName();
  
  public void setIndexName(String indexName);
  
  public Boolean getIsRuntimeMappingEnabled();
  
  public void setIsRuntimeMappingEnabled(Boolean isRuntimeMappingEnabled);
 
  public String getEndpointType();
  
  public void setEndpointType(String endpointType);
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
  
  public String getDashboardTabId();
  
  public void setDashboardTabId(String dashboardTabId);
  
  public List<String> getPhysicalCatalogs();
  
  public void setPhysicalCatalogs(List<String> physicalCatalogs);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
 public String getIconKey();
  
  public void setIconKey(String iconKey);
}
