package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;

public interface IGetReferencedNatureRelationshipModel
    extends IReferencedRelationshipModel, IKlassNatureRelationship {
  
  public static final String PROPERTY_COLLECTION_ID = "propertyCollectionId";
  public static final String NATURE_TYPE            = "natureType";
  
  public String getNatureType();
  
  public void setNatureType(String natureType);
  
  public String getPropertyCollectionId();
  
  public void setPropertyCollectionId(String propertyCollectionId);
}
