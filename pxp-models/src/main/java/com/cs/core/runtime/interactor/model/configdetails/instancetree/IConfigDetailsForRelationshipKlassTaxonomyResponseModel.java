package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.List;

import com.cs.core.config.interactor.entity.relationship.IRelationship;

public interface IConfigDetailsForRelationshipKlassTaxonomyResponseModel extends IConfigDetailsKlassTaxonomyTreeResponseModel {
  
  public static final String TARGET_IDS          = "targetIds";
  public static final String RELATIONSHIP_CONFIG = "relationshipConfig";
  
  public List<String> getTargetIds();
  public void setTargetIds(List<String> targetIds);
  
  public IRelationship getRelationshipConfig();
  public void setRelationshipConfig(IRelationship relationshipConfig);
    
}
