package com.cs.core.config.interactor.model.component;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IOffboardingRelationshipModel;

import java.util.List;

public interface IOffboardingRelationshipsResponseModel extends IModel {
  
  public static final String RELATIONSHIP_INSTANCES = "relationshipInstances";
  
  public List<IOffboardingRelationshipModel> getRelationshipInstances();
  
  public void setRelationshipInstances(List<IOffboardingRelationshipModel> relationshipInstances);
}
