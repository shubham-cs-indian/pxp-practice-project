package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ITargetRelationshipInstancesModel extends IModel {
  
  public static final String RELATIONSHIP_INSTANCES = "relationshipInstances";
  public static final String TOTAL_COUNT            = "totalCount";
  
  public List<IRelationshipInstance> getRelationshipInstances();
  
  public void setRelationshipInstances(List<IRelationshipInstance> relationshipInstances);
  
  public Long getTotalCount();
  
  public void setTotalCount(Long totalCount);
}
