package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGenerateSmartDocumentRequestModel extends IModel {
  
  public static final String KLASS_INSTANCE_IDS       = "klassInstanceIds";
  public static final String SMART_DOCUMENT_PRESET_ID = "smartDocumentPresetId";
  public static final String BASE_TYPE                = "baseType";
  
  public List<Long> getKlassInstanceIds();
  
  public void setKlassInstanceIds(List<Long> klassInstanceIds);
  
  public String getSmartDocumentPresetId();
  
  public void setSmartDocumentPresetId(String smartDocumentPresetId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
}
