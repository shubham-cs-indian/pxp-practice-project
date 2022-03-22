package com.cs.core.config.interactor.model.klass;

public class GetSectionInfoForRelationshipExportModel implements IGetSectionInfoForRelationshipExportModel{

  private static final long serialVersionUID = 1L;
  protected String klassId;
  protected String entityType;
  
  @Override
  public String getKlassId()
  {
    return klassId;
  }
  
  @Override
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
  }
  
  @Override
  public String getEntityType()
  {
    return entityType;
  }
  
  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }
}
