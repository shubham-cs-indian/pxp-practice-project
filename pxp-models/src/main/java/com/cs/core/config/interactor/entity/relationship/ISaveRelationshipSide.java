package com.cs.core.config.interactor.entity.relationship;

public interface ISaveRelationshipSide extends IRelationshipSide {
  
  public static final String ADDED_CONTEXT   = "addedContext";
  public static final String DELETED_CONTEXT = "deletedContext";
  
  public String getAddedContext();
  
  public void setAddedContext(String addedContext);
  
  public String getDeletedContext();
  
  public void setDeletedContext(String deletedContext);
}
