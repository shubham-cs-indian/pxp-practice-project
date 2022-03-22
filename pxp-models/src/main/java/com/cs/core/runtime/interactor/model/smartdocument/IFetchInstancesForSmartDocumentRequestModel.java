package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IFetchInstancesForSmartDocumentRequestModel extends IModel {
  
  public static final String INSTANCE_ID        = "instanceId";
  public static final String ENTITY_ID          = "entityId";
  public static final String FROM               = "from";
  public static final String SIZE               = "size";
  public static final String PRESET_CONFIG_DATA = "presetConfigData";
  public static final String SIDE2_KLASS_TYPE   = "side2KlassType";
  public static final String SIDE1_KLASS_TYPE   = "side1KlassType";
  
  public String getInstanceId();
  
  public void setInstanceId(String instanceId);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public int getFrom();
  
  public void setFrom(int from);
  
  public int getSize();
  
  public void setSize(int size);
  
  public IGetSmartDocumentPresetModel getPresetConfigData();
  
  public void setPresetConfigData(IGetSmartDocumentPresetModel presetConfigData);
  
  public String getSide1KlassType();
  
  public void setSide1KlassType(String side1KlassType);
  
  public String getSide2KlassType();
  
  public void setSide2KlassType(String side2KlassType);
}
