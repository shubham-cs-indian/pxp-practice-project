package com.cs.core.config.interactor.model.asset;

import com.cs.core.config.interactor.model.endpoint.EndpointModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class AssetExportAPIResponseModel implements IAssetExportAPIResponseModel {
  
  private static final long serialVersionUID                        = 1L;
  protected IEndpointModel  endpoint;
  protected boolean         shouldDownloadAssetWithOriginalFilename = false;
  protected boolean         isOrganizationCodeValid = true;
  protected boolean         isEndpointCodeValid = true;
  protected boolean         isLanguageCodeValid = true;

  @Override
  public IEndpointModel getEndpoint()
  {
    return endpoint;
  }
  
  @Override
  @JsonDeserialize(as = EndpointModel.class)
  public void setEndpoint(IEndpointModel endpoint)
  {
    this.endpoint = endpoint;
  }
  
  @Override
  public boolean getShouldDownloadAssetWithOriginalFilename()
  {
    return shouldDownloadAssetWithOriginalFilename;
  }
  
  @Override
  public void setShouldDownloadAssetWithOriginalFilename(boolean shouldDownloadAssetWithOriginalFilename)
  {
    this.shouldDownloadAssetWithOriginalFilename = shouldDownloadAssetWithOriginalFilename;
  }

  @Override
  public boolean getIsOrganizationCodeValid()
  {
    return isOrganizationCodeValid;
  }

  @Override
  public void setOrganizationCodeValid(boolean isOrganizationCodeValid)
  {
    this.isOrganizationCodeValid = isOrganizationCodeValid;
  }

  @Override
  public boolean getIsEndpointCodeValid()
  {
    return isEndpointCodeValid;
  }

  @Override
  public void setEndpointCodeValid(boolean isEndpointCodeValid)
  {
    this.isEndpointCodeValid = isEndpointCodeValid;
  }
  
  @Override
  public boolean getIsLanguageCodeValid()
  {
    return isLanguageCodeValid;
  }

  @Override
  public void setIsLanguageCodeValid(boolean isLanguageCodeValid)
  {
    this.isLanguageCodeValid = isLanguageCodeValid;
  }
  
}
