package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetInstancesForSmartDocumentRequestModel extends IModel {
  
  public static final String KLASS_INSTANCE_IDS                = "klassInstanceIds";
  public static final String SMART_DCOUMENT_PRESET_CONFIG_DATA = "smartDocumentPresetConfigData";
  public static final String BASE_TYPE                         = "baseType";
  
  public List<Long> getKlassInstanceIds();
  
  public void setKlassInstanceIds(List<Long> klassInstanceIds);
  
  public IGetSmartDocumentPresetModel getSmartDocumentPresetConfigData();
  
  public void setSmartDocumentPresetConfigData(
      IGetSmartDocumentPresetModel smartDocumentPresetConfigData);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
}
