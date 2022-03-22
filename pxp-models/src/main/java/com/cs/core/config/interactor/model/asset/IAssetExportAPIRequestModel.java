package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface IAssetExportAPIRequestModel extends IModel {
  public static final String ENDPOINT_CODE         = "endpointCode";
  public static final String ORAGANIZATION_CODE    = "organizationCode";
  public static final String LANGUAGE_CODE         = "languageCode";
  
  public String getEndpointCode();
  public void setEndpointCode(String endpointCode);
  
  public String getOrganizationCode();
  public void setOrganizationCode(String organizationCode);
  
  public String getLanguageCode();
  public void setLanguageCode(String languageCode);
  
}
