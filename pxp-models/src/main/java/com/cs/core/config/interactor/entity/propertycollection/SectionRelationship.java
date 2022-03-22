package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.config.interactor.entity.klass.KlassRelationshipSide;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SectionRelationship extends AbstractSectionElement implements ISectionRelationship {
  
  private static final long        serialVersionUID = 1L;
  
  protected IKlassRelationshipSide relationshipSide;
  protected IRelationship          relationship;
  protected String                 defaultValue     = "";
  protected Boolean                isNature;
  
  @Override
  public String getDefaultValue()
  {
    return defaultValue;
  }
  
  @Override
  public void setDefaultValue(String defaultValue)
  {
    this.defaultValue = defaultValue;
  }
  
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
  
  @Override
  public IRelationship getRelationship()
  {
    return relationship;
  }
  
  @JsonDeserialize(as = Relationship.class)
  @Override
  public void setRelationship(IRelationship relationship)
  {
    this.relationship = relationship;
  }
  
  @Override
  public Boolean getIsNature()
  {
    return isNature;
  }
  
  @Override
  public void setIsNature(Boolean isNature)
  {
    this.isNature = isNature;
  }
}
