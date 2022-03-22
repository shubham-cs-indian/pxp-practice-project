package com.cs.core.config.interactor.model.template;

public class ModifiedSequenceModel implements IModifiedSequenceModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected Integer         sequence;
  
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
  public Integer getSequence()
  {
    return sequence;
  }
  
  @Override
  public void setSequence(Integer sequence)
  {
    this.sequence = sequence;
  }
}
