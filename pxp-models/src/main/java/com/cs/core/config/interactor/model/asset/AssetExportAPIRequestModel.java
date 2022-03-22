package com.cs.core.config.interactor.model.asset;


public class AssetExportAPIRequestModel implements IAssetExportAPIRequestModel {

  private static final long serialVersionUID = 1L;
  private String            endpointCode;
  private String            organizationCode;
  private String            languageCode;

  @Override
  public String getEndpointCode()
  {
    return endpointCode;
  }

  @Override
  public void setEndpointCode(String endpointCode)
  {
    this.endpointCode = endpointCode;
  }

  @Override
  public String getOrganizationCode()
  {
    return organizationCode;
  }

  @Override
  public void setOrganizationCode(String organizationCode)
  {
    this.organizationCode = organizationCode;
  }
  
  @Override
  public String getLanguageCode()
  {
    return languageCode;
  }

  @Override
  public void setLanguageCode(String languageCode)
  {
    this.languageCode = languageCode;
  }
  
}
