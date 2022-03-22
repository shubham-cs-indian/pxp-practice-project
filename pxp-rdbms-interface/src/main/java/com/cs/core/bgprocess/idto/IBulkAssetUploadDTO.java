package com.cs.core.bgprocess.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IBulkAssetUploadDTO extends ISimpleDTO {

  public void setAssets(List<IAssetFileDTO> assetFiles);
  public List<IAssetFileDTO> getAssets();
  
  public String getLocaleID();
  public void setLocaleID(String localeID);
  
  public String getUserId();
  public void setUserId(String userId);
  
  public String getUserName();
  public void setUserName(String userName);
  
  public String getCatalogCode();
  public void setCatalogCode(String catalogCode);
  
  public String getPortalId();
  public void setPortalId(String portalId);
  
  public String getOrganizationCode();
  public void setOrganizationCode(String organizationCode);
  
  public String getEndpointId();
  public void setEndpointId(String endpointId);
  
  public String getLogicalCatalogId();
  public void setLogicalCatalogId(String logicalCatalogId);
  
  public String getParentTransactionId();
  public void setParentTransactionId(String parentTransactionId);
  
  public String getSystemId();
  public void setSystemId(String systemId);
  
  public boolean getShouldCheckForRedundancy();
  public void setShouldCheckForRedundancy(boolean shouldCheckForRedundancy);
  
  public List<String> getCollectionIds();
  public void setCollectionIds(List<String> collectionIds);
  
  public String getBaseType();
  public void setBaseType(String baseType);
 
  boolean getDetectDuplicate();
  void setDetectDuplicate(boolean detectDuplicate);
  
  public String getSide1InstanceId();
  public void setSide1InstanceId(String side1Instance);
  
  public String getSide1BaseType();
  public void setSide1BaseType(String baseType);
  
  public String getTabType();
  public void setTabType(String tabType);
  
  public String getTabId();
  public void setTabId(String tabId);
  
  String getRelationshipId();
  void setRelationshipId(String relationshipId);
  
  public String getRelationshipEntityId();
  public void setRelationshipEntityId(String relationshipEntityId);
 
  public String getSideId();
  public void setSideId(String sideId);
  
  public String getMode();
  public void setMode(String mode);
}
