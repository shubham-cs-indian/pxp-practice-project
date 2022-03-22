package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigDetailsForRelationshipKlassTaxonomyResponseModel extends ConfigDetailsKlassTaxonomyTreeResponseModel 
      implements IConfigDetailsForRelationshipKlassTaxonomyResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>                    targetIds;
  protected IRelationship                   relationshipConfig;
  
  @Override
  public List<String> getTargetIds()
  {
    if(targetIds == null) {
      targetIds = new ArrayList<>();
    }
    return targetIds;
  }
  
  @Override
  public void setTargetIds(List<String> targetIds)
  {
    this.targetIds = targetIds;
  }
  
  @Override
  public IRelationship getRelationshipConfig()
  {
    return this.relationshipConfig;
  }
  
  @JsonDeserialize(as = Relationship.class)
  @Override
  public void setRelationshipConfig(IRelationship relationshipConfig)
  {
    this.relationshipConfig = relationshipConfig;
  }
}
