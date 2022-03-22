package com.cs.core.config.interactor.model.component;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IOffboardingNatureRelationshipModel;

import java.util.List;

public interface IOffboardingNatureRelationshipsResponseModel extends IModel {
  
  public static final String RELATIONSHIP_INSTANCES = "natureRelationshipInstances";
  
  public List<IOffboardingNatureRelationshipModel> getNatureRelationshipInstances();
  
  public void setNatureRelationshipInstances(
      List<IOffboardingNatureRelationshipModel> relationshipInstances);
}
