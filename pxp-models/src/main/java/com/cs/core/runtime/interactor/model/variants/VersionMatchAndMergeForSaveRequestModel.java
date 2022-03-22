package com.cs.core.runtime.interactor.model.variants;

public class VersionMatchAndMergeForSaveRequestModel extends VersionMatchAndMergeDataRequestModel
    implements IVersionMatchAndMergeForSaveRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          versionInstanceId;
  
  @Override
  public String getVersionInstanceId()
  {
    return versionInstanceId;
  }
  
  @Override
  public void setVersionInstanceId(String versionInstanceId)
  {
    this.versionInstanceId = versionInstanceId;
  }
}
