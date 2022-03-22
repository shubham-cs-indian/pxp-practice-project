package com.cs.core.runtime.interactor.model.version;

import java.util.ArrayList;
import java.util.List;

public class IdAndRelationshipIdsModel implements IIdAndRelationshipIdsModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected List<String>    relationshipIds;
  
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
  public List<String> getRelationshipIds()
  {
    if (relationshipIds == null) {
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
