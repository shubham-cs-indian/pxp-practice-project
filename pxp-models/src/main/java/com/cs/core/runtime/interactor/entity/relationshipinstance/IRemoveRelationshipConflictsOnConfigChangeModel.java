package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import java.util.List;

public interface IRemoveRelationshipConflictsOnConfigChangeModel extends IModel {
  
  public static final String NATURE_RELATIONSHIP_ID      = "natureRelationshipId";
  public static final String PROPAGABLE_RELATIONSHIP_IDS = "propagableRelationshipIds";
  
  public String getNatureRelationshipId();
  
  public void setNatureRelationshipId(String natureRelationshipId);
  
  public List<String> getPropagableRelationshipIds();
  
  public void setPropagableRelationshipIds(List<String> propagableRelationshipIds);
}
