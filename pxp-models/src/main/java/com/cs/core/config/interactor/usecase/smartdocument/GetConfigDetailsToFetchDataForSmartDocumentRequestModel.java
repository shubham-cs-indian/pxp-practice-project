package com.cs.core.config.interactor.usecase.smartdocument;

public class GetConfigDetailsToFetchDataForSmartDocumentRequestModel
    implements IGetConfigDetailsToFetchDataForSmartDocumentRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          presetId;
  protected String          entityId;
  protected String          className;
  
  @Override
  public String getPresetId()
  {
    return presetId;
  }
  
  @Override
  public void setPresetId(String presetId)
  {
    this.presetId = presetId;
  }

  @Override
  public String getEntityId()
  {
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
  }
   
  @Override
  public String getClassName()
  {
    return className;
  }

  @Override
  public void setClassName(String className)
  {
    this.className = className;
  }
}
