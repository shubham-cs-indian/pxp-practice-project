package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.List;

import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenResponseModel;

public interface IConfigDetailsForGetRQFilterChildrenResponseModel extends IConfigDetailsForGetFilterChildrenResponseModel {
  
  public static final String TARGET_IDS          = "targetIds";
  public static final String IS_TARGET_TAXONOMY  = "isTargetTaxonomy";
  public static final String RELATIONSHIP_CONFIG = "relationshipConfig";
  
  public List<String> getTargetIds();
  public void setTargetIds(List<String> targetIds);
  
  public Boolean getIsTargetTaxonomy();
  public void setIsTargetTaxonomy(Boolean isTargetTaxonomy);
  
  public IRelationship getRelationshipConfig();
  public void setRelationshipConfig(IRelationship relationshipConfig);
  
}
