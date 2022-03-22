package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;

public interface IModifiedSectionRelationshipModel extends IModifiedSectionElementModel {
  
  public static final String RELATIONSHIP_SIDE = "relationshipSide";
  
  public IKlassRelationshipSide getRelationshipSide();
  
  public void setRelationshipSide(IKlassRelationshipSide relationshipSide);
}
