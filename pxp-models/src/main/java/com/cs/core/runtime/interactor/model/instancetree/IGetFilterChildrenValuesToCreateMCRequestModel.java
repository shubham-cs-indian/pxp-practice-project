package com.cs.core.runtime.interactor.model.instancetree;

public interface IGetFilterChildrenValuesToCreateMCRequestModel extends IGetFilterChildrenRequestModel {
  
  public static final String KLASS_ID        = "klassId";
  
  public String getKlassId();
  public void setKlassId(String klassId);
  
}
