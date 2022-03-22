package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IDiRelationshipsModel extends IModel {
  
  public static String ID              = "id";
  public static String RELATIONSHIP_ID = "relationshipId";
  public static String SIDE1ID         = "side1Id";
  public static String SIDE2ID         = "side2Id";
  public static String OPTIONAL        = "optional";
  
  public String getId();
  
  public void setId(String id);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public String getSide1Id();
  
  public void setSide1Id(String side1Id);
  
  public String getSide2Id();
  
  public void setSide2Id(String side2Id);
  
  public IDiOptionalModel getOptional();
  
  public void setOptional(IDiOptionalModel optional);
}
