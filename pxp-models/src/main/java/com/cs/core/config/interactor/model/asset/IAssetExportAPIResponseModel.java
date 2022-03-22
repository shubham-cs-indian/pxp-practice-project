package com.cs.core.config.interactor.model.asset;

import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetExportAPIResponseModel extends IModel {
  
  public static final String ENDPOINT                                     = "endpoint";
  public static final String SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME = "shouldDownloadAssetWithOriginalFilename";
  public static final String IS_ORGANIZATION_CODE_VALID                   = "isOrganizationCodeValid";
  public static final String IS_END_POINT_CODE_VALID                      = "isEndpointCodeValid";
  public static final String IS_LANGUAGE_CODE_VALID                       = "isLanguageCodeValid";
  
  public IEndpointModel getEndpoint();
  public void setEndpoint(IEndpointModel endpoint);

  public boolean getShouldDownloadAssetWithOriginalFilename();
  public void setShouldDownloadAssetWithOriginalFilename(boolean shouldDownloadAssetWithOriginalFilename);
  
  public boolean getIsOrganizationCodeValid();
  public void setOrganizationCodeValid(boolean isOrganizationCodeValid);
  
  public boolean getIsEndpointCodeValid();
  public void setEndpointCodeValid(boolean isEndpointCodeValid);
  
  public boolean getIsLanguageCodeValid();
  public void setIsLanguageCodeValid(boolean isLanguageCodeValid);
  
}
