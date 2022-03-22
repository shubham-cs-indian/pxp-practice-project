package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetEntityForSmartDocumentRequestModel extends IModel {
  
  public static final String INSTANCE_ID = "instanceId";
  public static final String PRESET_ID   = "presetId";
  public static final String ENTITY_ID   = "entityId";
  public static final String FROM        = "from";
  public static final String SIZE        = "size";
  
  public String getInstanceId();
  public void setInstanceId(String instanceId);
  
  public String getPresetId();
  public void setPresetId(String presetId);
  
  public String getEntityId();
  public void setEntityId(String entityId);
  
  public int getFrom();
  public void setFrom(int from);
  
  public int getSize();
  public void setSize(int size);
  
}
