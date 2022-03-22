package com.cs.core.runtime.interactor.model.smartdocument;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.config.interactor.entity.relationship.RelationshipInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetInstancesForSmartDocumentRelationshipFetchResponseModel
    extends GetInstancesForSmartDocumentResponseModel
    implements IGetInstancesForSmartDocumentRelationshipFetchResponseModel {
  
  private static final long             serialVersionUID      = 1L;
  protected List<IRelationshipInstance> relationshipInstances = new ArrayList<IRelationshipInstance>();
  
  @Override
  public List<IRelationshipInstance> getRelationshipInstances()
  {
    return relationshipInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = RelationshipInstance.class)
  public void setRelationshipInstances(List<IRelationshipInstance> relationshipInstances)
  {
    this.relationshipInstances = relationshipInstances;
  }
  
}
