package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.filter.KlassInstanceQuickListModel;

public class RelationshipInstanceQuickListModel extends KlassInstanceQuickListModel
    implements IRelationshipInstanceQuickListModel {
  
  private static final long serialVersionUID = 1L;
  protected String          klassType;
  protected String          relationshipSectionId;
  protected String          baseType;
  protected String          sideId;
  
  @Override
  public String getKlassType()
  {
    return klassType;
  }
  
  @Override
  public void setKlassType(String klassType)
  {
    this.klassType = klassType;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public String getRelationshipId()
  {
    return relationshipSectionId;
  }
  
  @Override
  public void setRelationshipId(String relationshipSectionId)
  {
    this.relationshipSectionId = relationshipSectionId;
  }
  
  @Override
  public String getSideId()
  {
    return sideId;
  }
  
  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }
  
}
