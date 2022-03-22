package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;

public class TaskReferanceTypeModel implements IIdLabelModel {
  
  private static final long serialVersionUID = 1L;
  String                    id;
  String                    label;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
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
}
