package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface ISectionRelationship extends ISectionElement {
  
  public static final String RELATIONSHIP_SIDE = "relationshipSide";
  public static final String RELATIONSHIP      = "relationship";
  public static final String DEFAULT_VALUE     = "defaultValue";
  public static final String IS_NATURE         = "isNature";
  
  public String getDefaultValue();
  
  public void setDefaultValue(String defaultValue);
  
  // UI requires this key
  public IKlassRelationshipSide getRelationshipSide();
  
  public void setRelationshipSide(IKlassRelationshipSide relationshipSide);
  
  public IRelationship getRelationship();
  
  public void setRelationship(IRelationship relationship);
  
  public Boolean getIsNature();
  
  public void setIsNature(Boolean isNature);
}
