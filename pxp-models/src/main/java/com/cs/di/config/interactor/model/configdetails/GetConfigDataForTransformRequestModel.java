package com.cs.di.config.interactor.model.configdetails;

import java.util.HashSet;
import java.util.Set;

public class GetConfigDataForTransformRequestModel implements IGetConfigDataForTransformRequestModel{

  private static final long serialVersionUID = 1L;
  
  protected Set<String>     contextIds       = new HashSet<>();
  protected Set<String>     relationshipIds  = new HashSet<>();
  
  @Override
  public Set<String> getContextIds()
  {
    return contextIds;
  }
  
  @Override
  public void setContextIds(Set<String> contextIds)
  {
    this.contextIds = contextIds;
  }
  
  @Override
  public Set<String> getRelationshipIds()
  {
    return relationshipIds;
  }
  
  @Override
  public void setRelationshipIds(Set<String> relationshipIds)
  {
    this.relationshipIds = relationshipIds;
  }
  
}
