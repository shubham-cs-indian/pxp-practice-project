package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;

public interface IReferencedNatureRelationshipModel extends IKlassNatureRelationship {
  
  public static final String NATURE_TYPE = "natureType";
  
  public String getNatureType();
  
  public void setNatureType(String natureType);
}
