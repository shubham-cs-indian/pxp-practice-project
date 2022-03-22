package com.cs.core.config.interactor.model.grid;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class WorkflowGridConfigGetAllRequestModel extends ConfigGetAllRequestModel implements IWorkflowGridConfigGetAllRequestModel{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  @JsonDeserialize(as = WorkflowGridFilterModel.class)
  @JsonSerialize(as = WorkflowGridFilterModel.class)
  private IWorkflowGridFilterModel filterData;
  
  @Override
  public IWorkflowGridFilterModel getWorkflowGridFilterModel()
  {
    return filterData;
  }
 
  @Override
  public void setWorkflowGridFilterModel(IWorkflowGridFilterModel filterData)
  {
    this.filterData = filterData;
  }
  
  
  
  
}
