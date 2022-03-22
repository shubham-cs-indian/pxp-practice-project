package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.config.interactor.entity.klass.KlassRelationshipSide;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ModifiedSectionRelationshipModel extends AbstractModifiedSectionElementModel
    implements IModifiedSectionRelationshipModel {
  
  private static final long        serialVersionUID = 1L;
  
  protected IKlassRelationshipSide relationshipSide;
  
  @Override
  public IKlassRelationshipSide getRelationshipSide()
  {
    return relationshipSide;
  }
  
  @JsonDeserialize(as = KlassRelationshipSide.class)
  @Override
  public void setRelationshipSide(IKlassRelationshipSide relationshipSide)
  {
    this.relationshipSide = relationshipSide;
  }
}
