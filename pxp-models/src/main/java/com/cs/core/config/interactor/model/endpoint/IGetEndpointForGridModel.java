package com.cs.core.config.interactor.model.endpoint;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;

public interface IGetEndpointForGridModel extends IGetEndpointModel, IConfigResponseWithAuditLogModel {
  
  public static final String CONFIG_DETAILS = "configDetails";
  
  public IConfigDetailsForGridEndpointsModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForGridEndpointsModel configDetails);
}
