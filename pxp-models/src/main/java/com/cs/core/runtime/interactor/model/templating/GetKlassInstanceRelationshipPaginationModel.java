package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.config.interactor.model.xray.XRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetKlassInstanceRelationshipPaginationModel extends
    GetKlassInstanceRelationshipTabModel implements IGetKlassInstanceRelationshipPaginationModel {
  
  private static final long         serialVersionUID = 1L;
  protected IXRayConfigDetailsModel xRayConfigDetails;
  
  @Override
  public IXRayConfigDetailsModel getXRayConfigDetails()
  {
    return xRayConfigDetails;
  }
  
  @Override
  @JsonDeserialize(as = XRayConfigDetailsModel.class)
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails)
  {
    this.xRayConfigDetails = xRayConfigDetails;
  }
}
