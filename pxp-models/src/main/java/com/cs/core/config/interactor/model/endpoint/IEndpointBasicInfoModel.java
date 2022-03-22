package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IEndpointBasicInfoModel extends IConfigEntityInformationModel {
  
  public static final String PHYSICAL_CATALOG_ID  = "physicalCatalogId";
  
  public String getPhysicalCatalogId();

  public void setPhysicalCatalogId(String physicalCatalogId);
}
