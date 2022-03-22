package com.cs.core.runtime.interactor.model.versionrollback;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IKlassInstanceVersionRollbackModel extends IModel {
  
  public static final String KLASS_INSTANCE_ID = "klassInstanceId";
  public static final String VERSION_ID        = "versionId";
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getVersionId();
  
  public void setVersionId(String versionId);
}
