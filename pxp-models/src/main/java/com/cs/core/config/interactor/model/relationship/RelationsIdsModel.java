package com.cs.core.config.interactor.model.relationship;

import java.util.ArrayList;
import java.util.List;

public class RelationsIdsModel implements IRelationsIdsModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    natureRelationshipIds;
  
  @Override
  public List<String> getNatureRelationshipIds()
  {
    if (natureRelationshipIds == null) {
      natureRelationshipIds = new ArrayList<String>();
    }
    return natureRelationshipIds;
  }
  
  @Override
  public void setNatureRelationshipIds(List<String> natureRelationshipIds)
  {
    this.natureRelationshipIds = natureRelationshipIds;
  }
}
