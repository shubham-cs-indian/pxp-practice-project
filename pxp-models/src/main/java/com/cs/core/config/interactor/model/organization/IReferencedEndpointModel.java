package com.cs.core.config.interactor.model.organization;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IReferencedEndpointModel extends IIdLabelCodeModel {
  
  public static final String SYSTEM_ID = "systemId";
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
}
