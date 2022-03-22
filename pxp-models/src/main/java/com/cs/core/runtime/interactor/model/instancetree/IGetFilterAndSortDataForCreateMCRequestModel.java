package com.cs.core.runtime.interactor.model.instancetree;

public interface IGetFilterAndSortDataForCreateMCRequestModel extends IGetNewFilterAndSortDataRequestModel {
  
  public static final String KLASS_ID = "klassId";
  
  public void setKlassId(String klassId);
  public String getKlassId();
}

