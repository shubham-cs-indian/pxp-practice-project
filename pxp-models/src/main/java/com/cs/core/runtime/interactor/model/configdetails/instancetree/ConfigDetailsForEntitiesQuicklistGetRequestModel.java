package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import com.cs.core.runtime.interactor.model.configdetails.ConfigDetailsForInstanceTreeGetRequestModel;

public class ConfigDetailsForEntitiesQuicklistGetRequestModel extends ConfigDetailsForInstanceTreeGetRequestModel
    implements IConfigDetailsForEntitiesQuicklistGetRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String                                  entityId;
  protected String                                  contextId;

  @Override
  public String getEntityId()
  {
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
  }
  
  @Override
  public String getContextId()
  {
    return contextId;
  }

  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
}
