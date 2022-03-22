package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;

public class EndpointBasicInfoModel extends ConfigEntityInformationModel
    implements IEndpointBasicInfoModel {
  
  private static final long serialVersionUID = 1L;
  protected String               physicalCatalogId;
  
  @Override
  public String getPhysicalCatalogId()
  {
    return physicalCatalogId;
  }

  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }
}
