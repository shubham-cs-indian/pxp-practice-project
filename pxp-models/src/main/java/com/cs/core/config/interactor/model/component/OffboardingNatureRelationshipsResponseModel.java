package com.cs.core.config.interactor.model.component;

import com.cs.core.runtime.interactor.model.relationship.IOffboardingNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.OffboardingNatureRelationshipModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class OffboardingNatureRelationshipsResponseModel
    implements IOffboardingNatureRelationshipsResponseModel {
  
  private static final long                 serialVersionUID = 1L;
  List<IOffboardingNatureRelationshipModel> natureRelationshipInstances;
  
  @Override
  public List<IOffboardingNatureRelationshipModel> getNatureRelationshipInstances()
  {
    return this.natureRelationshipInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = OffboardingNatureRelationshipModel.class)
  public void setNatureRelationshipInstances(
      List<IOffboardingNatureRelationshipModel> natureRelationshipInstances)
  {
    this.natureRelationshipInstances = natureRelationshipInstances;
  }
}
