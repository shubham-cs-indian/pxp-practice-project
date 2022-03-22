package com.cs.core.runtime.interactor.model.relationship;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ReferencedRelationshipPropertiesModel
    implements IReferencedRelationshipPropertiesModel {
  
  private static final long                  serialVersionUID = 1L;
  
  protected IRelationshipSidePropertiesModel side1;
  protected IRelationshipSidePropertiesModel side2;
  protected String                           label;
  protected String                           id;
  protected String                           code;
  protected String                           relationshipType;
  
  @Override
  public IRelationshipSidePropertiesModel getSide1()
  {
    return side1;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipSidePropertiesModel.class)
  public void setSide1(IRelationshipSidePropertiesModel side1)
  {
    this.side1 = side1;
  }
  
  @Override
  public IRelationshipSidePropertiesModel getSide2()
  {
    return side2;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipSidePropertiesModel.class)
  public void setSide2(IRelationshipSidePropertiesModel side2)
  {
    this.side2 = side2;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getRelationshipType()
  {
    return relationshipType;
  }
  
  @Override
  public void setRelationshipType(String relationshipType)
  {
    this.relationshipType = relationshipType;
  }
}
