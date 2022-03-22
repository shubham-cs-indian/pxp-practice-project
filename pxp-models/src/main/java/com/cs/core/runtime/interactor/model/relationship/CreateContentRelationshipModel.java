package com.cs.core.runtime.interactor.model.relationship;

public class CreateContentRelationshipModel implements ICreateContentRelationshipModel {
  
  protected String id;
  protected String type;
  
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
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
}
