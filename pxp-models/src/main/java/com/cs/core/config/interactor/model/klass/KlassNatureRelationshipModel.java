package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.KlassNatureRelationship;

public class KlassNatureRelationshipModel extends KlassNatureRelationship
    implements IKlassNatureRelationshipModel {
  
  private static final long serialVersionUID = 1L;
  protected String          targetRelationshipMappingId;
  
  @Override
  public String getTargetRelationshipMappingId()
  {
    return targetRelationshipMappingId;
  }
  
  @Override
  public void setTargetRelationshipMappingId(String targetRelationshipMappingId)
  {
    this.targetRelationshipMappingId = targetRelationshipMappingId;
  }
}
