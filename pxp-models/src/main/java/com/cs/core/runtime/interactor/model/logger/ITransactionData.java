package com.cs.core.runtime.interactor.model.logger;

import java.util.List;

public interface ITransactionData extends ILogData {
  
  public static final String PHYSICAL_CATALOG_ID = "physicalCatalogId";
  public static final String PORTAL_ID           = "portalId";
  public static final String ORGANIZATION_ID     = "organizationId";
  public static final String LOGICAL_CATALOG_ID  = "logicalCatalogId";
  public static final String USER_ID             = "userId";
  public static final String UI_LANGUAGE         = "uiLanguage";
  public static final String DATA_LANGUAGE       = "dataLanguage";
  public static final String LEVEL               = "level";
  public static final String ENDPOINT_ID         = "endpointId";
  
  public InteractorData getInteractorData();
  
  public void setInteractorData(InteractorData interactorData);
  
  public String getRequestId();
  
  public void setRequestId(String requestId);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public Throwable getException();
  
  public void setException(Throwable exception);
  
  public String getUserName();
  
  public void setUserName(String userName);
  
  public List<String> getSuccessSubJobIds();
  
  public void setSuccessSubJobIds(List<String> successSubJobIds);
  
  public List<String> getSubJobIds();
  
  public void setSubJobIds(List<String> subJobIds);
  
  public String getUiLanguage();
  
  public void setUiLanguage(String uiLanguage);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getPortalId();
  
  public void setPortalId(String portalId);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getLogicalCatalogId();
  
  public void setLogicalCatalogId(String logicalCatalogId);
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
  
  public String getEndpointType();
  
  public void setEndpointType(String systemId);
  
  public String getUrlForTalend();
  
  public void setUrlForTalend(String urlForTalend);
  
  public String getDataLanguage();
  
  public void setDataLanguage(String dataLanguage);
  
  public Long getLevel();
  
  public void setLevel(Long level);
}
