package com.cs.core.runtime.interactor.model.taskexecutor;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetImportedEntityStatusResponseModel extends IModel {
  
  public static final String SUCCESS_IDS            = "successIds";
  public static final String FAILED_IDS             = "failedIds";
  public static final String IS_COMPONENT_COMPLETED = "isComponentCompleted";
  public static final String NUMBER_OF_DOCUMENTS    = "numberOfDocuments";
  
  public List<String> getSuccessIds();
  
  public void setSuccessIds(List<String> successIds);
  
  public List<String> getFailedIds();
  
  public void setFailedIds(List<String> failedIds);
  
  public Boolean getIsComponentCompleted();
  
  public void setIsComponentCompleted(Boolean isComponentCompleted);
  
  public Long getNumberOfDocuments();
  
  public void setNumberOfDocuments(Long numberOfDocuments);
}
