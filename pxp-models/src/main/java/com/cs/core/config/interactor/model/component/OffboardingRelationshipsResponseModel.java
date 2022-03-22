package com.cs.core.config.interactor.model.component;

import com.cs.core.runtime.interactor.model.relationship.IOffboardingRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.OffboardingRelationshipModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class OffboardingRelationshipsResponseModel
    implements IOffboardingRelationshipsResponseModel {
  
  private static final long           serialVersionUID = 1L;
  List<IOffboardingRelationshipModel> relationshipInstances;
  
  @Override
  public List<IOffboardingRelationshipModel> getRelationshipInstances()
  {
    return this.relationshipInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = OffboardingRelationshipModel.class)
  public void setRelationshipInstances(List<IOffboardingRelationshipModel> relationshipInstances)
  {
    this.relationshipInstances = relationshipInstances;
  }
}
