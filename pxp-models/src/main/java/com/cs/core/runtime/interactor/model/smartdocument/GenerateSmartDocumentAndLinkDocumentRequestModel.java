package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;

public class GenerateSmartDocumentAndLinkDocumentRequestModel extends IdsListParameterModel
    implements IGenerateSmartDocumentAndLinkDocumentRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected IMultiPartFileInfoModel multiPartFileInfoModel;
  protected String                  baseType;
  protected String                  presetLanguageCode;
  
  @Override
  public IMultiPartFileInfoModel getMultiPartFileInfoModel()
  {
    return multiPartFileInfoModel;
  }
  
  @Override
  public void setMultiPartFileInfoModel(IMultiPartFileInfoModel assetKeysModel)
  {
    this.multiPartFileInfoModel = assetKeysModel;
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
}
