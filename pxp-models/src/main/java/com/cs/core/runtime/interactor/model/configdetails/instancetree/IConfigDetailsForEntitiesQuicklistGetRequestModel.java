package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetRequestModel;

public interface IConfigDetailsForEntitiesQuicklistGetRequestModel extends IConfigDetailsForInstanceTreeGetRequestModel {
  
  public static final String ENTITY_ID                      = "entityId";
  public static final String CONTEXT_ID                     = "contextId";
  
  public String getEntityId();
  public void setEntityId(String entityId);
  
  public String getContextId();
  public void setContextId(String contextId);
  
  
}
