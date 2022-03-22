package com.cs.core.runtime.interactor.model.assetinstance;

public class AssetInstanceExportRequestModel implements IAssetInstanceExportRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          fileName         = "";
  protected String          catalogCode      = "";
  protected String          endpointCode     = "";
  protected String          assetInstanceId;
  protected String          organizationCode = "";
  protected String          languageCode;

  @Override
  public String getFileName()
  {
    return fileName;
  }
  
  @Override
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
  
  @Override
  public String getCatalogCode()
  {
    return catalogCode;
  }
  
  @Override
  public void setCatalogCode(String catalogCode)
  {
    this.catalogCode = catalogCode;
  }
  
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
  public String getAssetInstanceId()
  {
    return assetInstanceId;
  }
  
  @Override
  public void setAssetInstanceId(String assetInstanceId)
  {
    this.assetInstanceId = assetInstanceId;
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
