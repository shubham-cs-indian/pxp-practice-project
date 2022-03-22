package com.cs.di.config.interactor.model.initializeworflowevent;

import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;

import java.util.List;

public interface IDeleteProcessEventResponseModel extends IBulkDeleteReturnModel {
  
  public static final String PROCESS_DEFINATION_IDS = "processDefinationIds";
  
  public List<String> getProcessDefinationIds();
  
  public void setProcessDefinationIds(List<String> processDefinationIds);
}
