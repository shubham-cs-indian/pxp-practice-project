package com.cs.core.config.interactor.model.asset;

public interface IUploadSmartDocumentModel extends IUploadAssetModel {
  
  public static final String PRESET_LANGUAGE_CODE = "presetLanguageCode";
  public static final String BASE_TYPE            = "baseType";
  
  public String getPresetLanguageCode();
  public void setPresetLanguageCode(String presetLanguageCode);
  
  public String getBaseType();
  public void setBaseType(String baseType);
}
