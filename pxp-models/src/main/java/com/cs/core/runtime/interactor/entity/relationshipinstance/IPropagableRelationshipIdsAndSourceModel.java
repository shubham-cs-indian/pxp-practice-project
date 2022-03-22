package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import java.util.List;

public interface IPropagableRelationshipIdsAndSourceModel extends IModel {
  
  public static final String PROPAGABLE_RELATIONHSHIP_SIDE_IDS = "propagableRelationshipSideIds";
  public static final String DELETED_SOURCE_INSTANCE_IDS       = "deletedSourceInstanceIds";
  
  public List<String> getPropagableRelationshipSideIds();
  
  public void setPropagableRelationshipSideIds(List<String> propagableRelationshipSideIds);
  
  public List<String> getDeletedSourceInstanceIds();
  
  public void setDeletedSourceInstanceIds(List<String> deletedSourceInstanceIds);
}
