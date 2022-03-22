package com.cs.core.runtime.interactor.model.smartdocument;

public class GetEntityForSmartDocumentRequestModel implements IGetEntityForSmartDocumentRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          instanceId;
  protected String          presetId;
  protected String          entityId;
  protected int             from;
  protected int             size;
  
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
  
}
