package com.cs.core.config.interactor.model.asset;

public class UploadSmartDocumentModel extends UploadAssetModel implements IUploadSmartDocumentModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          presetLanguageCode;
  protected String          baseType;

  @Override
  public String getPresetLanguageCode()
  {
    return presetLanguageCode;
  }

  @Override
  public void setPresetLanguageCode(String presetLanguageCode)
  {
    this.presetLanguageCode = presetLanguageCode;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }

  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
}