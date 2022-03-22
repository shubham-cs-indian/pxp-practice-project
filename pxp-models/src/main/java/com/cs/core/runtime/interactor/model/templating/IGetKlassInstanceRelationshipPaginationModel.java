package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;

public interface IGetKlassInstanceRelationshipPaginationModel
    extends IGetKlassInstanceRelationshipTabModel {
  
  public static final String X_RAY_CONFIG_DETAILS = "xrayConfigDetails";
  
  public IXRayConfigDetailsModel getXRayConfigDetails();
  
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails);
}
