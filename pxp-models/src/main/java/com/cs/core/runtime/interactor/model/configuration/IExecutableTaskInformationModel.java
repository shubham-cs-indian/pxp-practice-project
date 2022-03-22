package com.cs.core.runtime.interactor.model.configuration;

public interface IExecutableTaskInformationModel extends IModel {
  
  public static String TASK_CLASS = "taskClass";
  public static String LABEL      = "label";
  public static String PARAMETERS = "parameters";
  
  public String getTaskClass();
  
  public void setTaskClass(String taskClass);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String[] getParameters();
  
  public void setParameters(String[] parameters);
}
