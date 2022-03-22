package com.cs.core.runtime.interactor.entity.relationshipinstance;

import java.util.ArrayList;
import java.util.List;

public class ConfigChangeRelationshipInheritanceModel implements IConfigChangeRelationshipInheritanceModel{
  
  private static final long serialVersionUID = 1L;
 
  protected List<String> relationshipIds;
  
  
  public ConfigChangeRelationshipInheritanceModel(List<String> relationshipIds)
  {
    this.relationshipIds = relationshipIds;
  }
  
  @Override
  public List<String> getRelationshipIds()
  {
    if(relationshipIds == null) {
      relationshipIds = new ArrayList<String>();
    }
    return relationshipIds;
  }
  
  @Override
  public void setRelationshipIds(List<String> relationshipIds)
  {
    this.relationshipIds = relationshipIds;
  }
  
  
}

