package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRelationshipIdSourceModel extends IModel {
  
  public static final String RELATIONSHIP_ID         = "relationshipId";
  public static final String SOURCE_KLASSINSTANCE_ID = "sourceKlassInstanceId";
  public static final String SIDE_ID                 = "sideId";
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public String getSourceKlassInstanceId();
  
  public void setSourceKlassInstanceId(String sourceKlassInstanceId);
  
  public String getSideId();
  
  public void setSideId(String sideId);
}
