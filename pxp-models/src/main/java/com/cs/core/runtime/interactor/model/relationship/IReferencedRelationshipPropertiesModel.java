package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IReferencedRelationshipPropertiesModel extends IModel {
  
  public static final String ID                = "id";
  public static final String CODE              = "code";
  public static final String RELATIONSHIP_TYPE = "relationshipType";
  public static final String SIDE1             = "side1";
  public static final String SIDE2             = "side2";
  public static final String LABEL             = "label";
  
  public String getId();
  
  public void setId(String id);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getRelationshipType();
  
  public void setRelationshipType(String relationshipType);
  
  public IRelationshipSidePropertiesModel getSide1();
  
  public void setSide1(IRelationshipSidePropertiesModel side1);
  
  public IRelationshipSidePropertiesModel getSide2();
  
  public void setSide2(IRelationshipSidePropertiesModel side2);
  
  public String getLabel();
  
  public void setLabel(String label);
}
