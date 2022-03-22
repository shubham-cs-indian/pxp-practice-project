package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetInstanceExportRequestModel extends IModel {
  
  public static final String ORIGINAL_FILENAME  = "original";
  public static final String ASSETNAME_FILENAME = "assetname";
  
  public static final String FILE_NAME          = "fileName";
  public static final String CATALOG_CODE       = "catalogCode";
  public static final String ENDPOINT_CODE      = "endpointCode";
  public static final String ASSET_INSTANCE_ID  = "assetInstanceId";
  public static final String ORGANIZATION_CODE  = "organizationCode";
  public static final String LANGUAGE_CODE      = "languageCode";
  
  public String getFileName();
  public void setFileName(String fileName);
  
  public String getCatalogCode();
  public void setCatalogCode(String catalogCode);
  
  public String getEndpointCode();
  public void setEndpointCode(String endpointCode);
  
  public String getAssetInstanceId();
  public void setAssetInstanceId(String assetInstanceId);
  
  public String getOrganizationCode();
  public void setOrganizationCode(String organizationCode);
  
  public String getLanguageCode();
  public void setLanguageCode(String languageCode);
  
}
