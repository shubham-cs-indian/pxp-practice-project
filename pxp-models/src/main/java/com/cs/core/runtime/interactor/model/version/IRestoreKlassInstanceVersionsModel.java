package com.cs.core.runtime.interactor.model.version;

public interface IRestoreKlassInstanceVersionsModel extends IMoveKlassInstanceVersionsModel {
  
  public static final String NUM_OF_VERSIONS_TO_MAINTAIN = "numOfVersionsToMaintain";
  
  public Integer getNumOfVersionsToMaintain();
  
  public void setNumOfVersionsToMaintain(Integer numOfVersionsToMaintain);
}
