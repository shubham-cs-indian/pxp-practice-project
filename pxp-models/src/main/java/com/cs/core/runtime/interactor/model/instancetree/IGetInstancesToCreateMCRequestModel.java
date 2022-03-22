package com.cs.core.runtime.interactor.model.instancetree;

import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;

public interface IGetInstancesToCreateMCRequestModel extends IGetInstanceTreeRequestModel {
  
  public static final String KLASS_ID        = "klassId";
  
  public String getKlassId();
  public void setKlassId(String klassId);
  
}
