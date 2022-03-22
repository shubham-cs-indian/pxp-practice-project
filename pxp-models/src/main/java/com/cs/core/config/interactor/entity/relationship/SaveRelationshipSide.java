package com.cs.core.config.interactor.entity.relationship;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SaveRelationshipSide extends RelationshipSide implements ISaveRelationshipSide {
  
  private static final long serialVersionUID = 1L;
  protected String          addedContext;
  protected String          deletedContext;
  
  @Override
  public String getAddedContext()
  {
    return addedContext;
  }
  
  @Override
  public void setAddedContext(String addedContext)
  {
    this.addedContext = addedContext;
  }
  
  @Override
  public String getDeletedContext()
  {
    return deletedContext;
  }
  
  @Override
  public void setDeletedContext(String deletedContext)
  {
    this.deletedContext = deletedContext;
  }
  
  @JsonIgnore
  @Override
  public String getContextId()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setContextId(String context)
  {
  }
}
