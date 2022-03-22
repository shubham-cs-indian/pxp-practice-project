package com.cs.core.config.interactor.usecase.smartdocument;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigDetailsToFetchDataForSmartDocumentRequestModel extends IModel {
  
  public static final String PRESET_ID  = "presetId";
  public static final String ENTITY_ID  = "entityId";
  public static final String CLASS_NAME = "className";
  
  
  public String getPresetId();
  
  public void setPresetId(String presetId);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);

  public String getClassName();

  public void setClassName(String className);
}
