package com.cs.core.config.interactor.model.tabs;

public class ModifiedTabPropertySequenceModel implements IModifiedTabPropertySequenceModel {
  
  private static final long serialVersionUID = 1L;
  protected Integer         newSequence;
  protected String          id;
  
  @Override
  public Integer getNewSequence()
  {
    return newSequence;
  }
  
  @Override
  public void setNewSequence(Integer newSequence)
  {
    this.newSequence = newSequence;
  }
  
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
}
