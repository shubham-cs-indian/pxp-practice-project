package com.cs.core.runtime.interactor.model.configdetails.instancetree;

public interface IConfigDetailsForGetTargetToCreateMCRequestModel extends IGetConfigDetailsForGetNewInstanceTreeRequestModel {
  
  public static final String KLASS_ID         = "klassId";
  
  public String getKlassId();
  public void setKlassId(String targetId);
  
}
