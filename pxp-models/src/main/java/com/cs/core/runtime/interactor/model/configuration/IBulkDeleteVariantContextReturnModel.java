package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public interface IBulkDeleteVariantContextReturnModel extends IBulkDeleteReturnModel {
  
  String RELATIONSHIP_IDS = "relationshipIds";
  
  public List<String> getRelationshipIds();
  
  public void setRelationshipIds(List<String> relationshipIds);
}
