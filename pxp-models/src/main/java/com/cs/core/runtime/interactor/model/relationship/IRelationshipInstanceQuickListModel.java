package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.filter.IKlassInstanceQuickListModel;

public interface IRelationshipInstanceQuickListModel extends IKlassInstanceQuickListModel {
  
  public static final String RELATIONSHIP_ID = "relationshipId";
  public static final String KLASS_TYPE      = "klassType";
  public static final String BASE_TYPE       = "baseType";
  public static final String SIDE_ID         = "sideId";
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipSectionId);
  
  public String getKlassType();
  
  public void setKlassType(String klassType);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getSideId();
  
  public void setSideId(String sideId);
  
}
