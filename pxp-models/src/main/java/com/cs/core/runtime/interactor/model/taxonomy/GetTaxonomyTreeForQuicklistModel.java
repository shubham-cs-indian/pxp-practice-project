package com.cs.core.runtime.interactor.model.taxonomy;

public class GetTaxonomyTreeForQuicklistModel extends GetTaxonomyTreeModel
    implements IGetTaxonomyTreeForQuicklistModel {
  
  private static final long serialVersionUID = 1L;
  protected String          relationshipId;
  protected String          typeKlassId;
  protected String          targetType;
  protected String          sideId;
  protected String          referenceId;
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
  
  @Override
  public String getTypeKlassId()
  {
    return typeKlassId;
  }
  
  @Override
  public void setTypeKlassId(String typeKlassId)
  {
    this.typeKlassId = typeKlassId;
  }
  
  @Override
  public String getTargetType()
  {
    return targetType;
  }
  
  @Override
  public void setTargetType(String targetType)
  {
    this.targetType = targetType;
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
  
  @Override
  public String getReferenceId()
  {
    return referenceId;
  }
  
  @Override
  public void setReferenceId(String referenceId)
  {
    this.referenceId = referenceId;
  }
}
