package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;

import java.util.List;

public interface IGetInstancesForSmartDocumentRelationshipFetchResponseModel
    extends IGetInstancesForSmartDocumentResponseModel {
  
  public static String RELATIONSHIP_INSTANCES = "relationshipInstances";
  
  public List<IRelationshipInstance> getRelationshipInstances();
  
  public void setRelationshipInstances(List<IRelationshipInstance> relationshipInstances);
}
