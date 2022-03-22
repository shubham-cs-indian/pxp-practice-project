package com.cs.core.runtime.interactor.model.version;

public class RestoreKlassInstanceVersionsModel extends MoveKlassInstanceVersionsModel
    implements IRestoreKlassInstanceVersionsModel {
  
  private static final long serialVersionUID        = 1L;
  protected Integer         numOfVersionsToMaintain = 0;
  
  public RestoreKlassInstanceVersionsModel(IMoveKlassInstanceVersionsModel dataModel)
  {
    this.instanceId = dataModel.getInstanceId();
    this.versionNumbers = dataModel.getVersionNumbers();
  }
  
  @Override
  public Integer getNumOfVersionsToMaintain()
  {
    return numOfVersionsToMaintain;
  }
  
  @Override
  public void setNumOfVersionsToMaintain(Integer numOfVersionsToMaintain)
  {
    this.numOfVersionsToMaintain = numOfVersionsToMaintain;
  }
}
