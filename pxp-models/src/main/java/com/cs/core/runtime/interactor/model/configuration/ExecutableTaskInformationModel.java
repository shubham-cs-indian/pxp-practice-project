package com.cs.core.runtime.interactor.model.configuration;

public class ExecutableTaskInformationModel implements IExecutableTaskInformationModel {
  
  protected String   taskClass;
  protected String   label;
  protected String[] parameters;
  
  @Override
  public String getTaskClass()
  {
    return taskClass;
  }
  
  @Override
  public void setTaskClass(String taskClass)
  {
    this.taskClass = taskClass;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String[] getParameters()
  {
    return parameters;
  }
  
  @Override
  public void setParameters(String[] parameters)
  {
    this.parameters = parameters;
  }
}
