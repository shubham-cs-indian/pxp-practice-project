package com.cs.core.runtime.interactor.model.versionrollback;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForVersionRollbackModel;

public interface IRollbackInstanceStrategyRequestModel extends IModel {
  
  public static final String ID             = "id";
  public static final String VERSION_ID     = "versionId";
  public static final String CONFIG_DETAILS = "configDetails";
  
  public String getId();
  
  public void setId(String id);
  
  public String getVersionId();
  
  public void setVersionId(String versionId);
  
  public IGetConfigDetailsForVersionRollbackModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsForVersionRollbackModel configDetails);
}
