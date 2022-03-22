package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForVersionRollbackModel;

public interface IRestoreVariantRequestModel extends IModel {
  
  public static String ID             = "id";
  public static String CONFIG_DETAILS = "configDetails";
  
  public String getId();
  
  public void setId(String id);
  
  public IGetConfigDetailsForVersionRollbackModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsForVersionRollbackModel configDetails);
}
