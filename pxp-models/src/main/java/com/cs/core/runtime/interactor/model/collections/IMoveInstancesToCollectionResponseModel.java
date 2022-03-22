package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

import java.util.List;

public interface IMoveInstancesToCollectionResponseModel extends IModel {
  
  public static final String ID          = "id";
  public static final String LABEL       = "label";
  public static final String SUCCESS_IDS = "successIds";
  public static final String FAILURE     = "failure";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<String> getSuccessIds();
  
  public void setSuccessIds(List<String> successids);
  
  public IExceptionModel getFailure();
  
  public void setFailure(IExceptionModel failure);
}
