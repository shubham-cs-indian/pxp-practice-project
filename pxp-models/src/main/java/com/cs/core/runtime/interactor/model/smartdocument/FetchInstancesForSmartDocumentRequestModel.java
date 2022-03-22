package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.preset.GetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class FetchInstancesForSmartDocumentRequestModel
    implements IFetchInstancesForSmartDocumentRequestModel {
  
  private static final long              serialVersionUID = 1L;
  protected String                       instanceId;
  protected String                       entityId;
  protected int                          from;
  protected int                          size;
  protected IGetSmartDocumentPresetModel presetConfigData;
  protected String                       side1KlassType;
  protected String                       side2KlassType;
  
  @Override
  public String getInstanceId()
  {
    return instanceId;
  }
  
  @Override
  public void setInstanceId(String instanceId)
  {
    this.instanceId = instanceId;
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
  public int getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(int from)
  {
    this.from = from;
  }
  
  @Override
  public int getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(int size)
  {
    this.size = size;
  }
  
  @Override
  public IGetSmartDocumentPresetModel getPresetConfigData()
  {
    return presetConfigData;
  }
  
  @Override
  @JsonDeserialize(as=GetSmartDocumentPresetModel.class)
  public void setPresetConfigData(IGetSmartDocumentPresetModel presetConfigData)
  {
    this.presetConfigData = presetConfigData;
  }
  
  @Override
  public String getSide1KlassType()
  {
    return side1KlassType;
  }

  @Override
  public void setSide1KlassType(String side1KlassType)
  {
    this.side1KlassType = side1KlassType;
  }
  
  @Override
  public String getSide2KlassType()
  {
    return side2KlassType;
  }
  
  @Override
  public void setSide2KlassType(String side2KlassType)
  {
    this.side2KlassType = side2KlassType;
  }
}
