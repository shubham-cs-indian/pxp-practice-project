package com.cs.core.runtime.interactor.model.rollback;

import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;

public class KlassInstanceVersionRollbackModel implements IKlassInstanceVersionRollbackModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          klassInstanceId;
  protected String          versionId;
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public String getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(String versionId)
  {
    this.versionId = versionId;
  }
}
