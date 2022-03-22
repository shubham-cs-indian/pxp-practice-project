package com.cs.di.config.interactor.model.initializeworflowevent;

import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;

import java.util.ArrayList;
import java.util.List;

public class DeleteProcessesEventResponseModel extends BulkDeleteReturnModel
    implements IDeleteProcessEventResponseModel {
  
  private static final long serialVersionUID     = 1L;
  protected List<String>    processDefinationIds = new ArrayList<>();
  
  @Override
  public List<String> getProcessDefinationIds()
  {
    return processDefinationIds;
  }
  
  @Override
  public void setProcessDefinationIds(List<String> processDefinationIds)
  {
    this.processDefinationIds = processDefinationIds;
  }
}
