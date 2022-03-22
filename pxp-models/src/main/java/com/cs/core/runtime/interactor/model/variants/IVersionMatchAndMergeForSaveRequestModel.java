package com.cs.core.runtime.interactor.model.variants;

public interface IVersionMatchAndMergeForSaveRequestModel
    extends IVersionMatchAndMergeDataRequestModel {
  
  public static final String VERSION_INSTANCE_ID = "versionInstanceId";
  
  public String getVersionInstanceId();
  
  public void setVersionInstanceId(String versionInstanceId);
}
