package com.cs.core.config.strategy.model.context;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigDetailsForLinkedVariantDuplicateCheckResponseModel extends IModel {
  
  public static final String IS_DUPLICATION_ALLOWED = "isDuplicationAllowed";
  public static final String RELATIONSHIP_IID       = "relationshipIId";
  
  public Boolean getIsDuplicationAllowed();
  public void setIsDuplicationAllowed(Boolean isDuplicationAllowed);
  
  public Long getRelationshipIId();
  public void setRelationshipIId(Long rleationshipIId);
  
}
