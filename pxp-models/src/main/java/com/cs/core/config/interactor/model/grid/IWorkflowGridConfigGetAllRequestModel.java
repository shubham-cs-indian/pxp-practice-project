package com.cs.core.config.interactor.model.grid;


public interface IWorkflowGridConfigGetAllRequestModel extends IConfigGetAllRequestModel{
  
  public static String  WORKFLOW_GRID_FILTER_MODEL = "workflowGridFilterModel"; 
  
  public IWorkflowGridFilterModel getWorkflowGridFilterModel();
  public void setWorkflowGridFilterModel(IWorkflowGridFilterModel filterData);
  
}
