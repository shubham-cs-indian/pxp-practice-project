package com.cs.core.runtime.interactor.model.dynamichierarchy;

public class IdLabelModel implements IIdLabelModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          label;
  
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
