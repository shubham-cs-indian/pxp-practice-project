package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGenerateSmartDocumentAndLinkDocumentRequestModel extends IIdsListParameterModel {
  
  public static final String MULTI_PART_FILE_INFO_MODEL = "multiPartFileInfoModel";
  public static final String BASE_TYPE                  = "baseType";
  public static final String PRESET_LANGUAGE_CODE       = "presetLanguageCode";
  
  public IMultiPartFileInfoModel getMultiPartFileInfoModel();
  public void setMultiPartFileInfoModel(IMultiPartFileInfoModel assetKeysModel);
  
  public String getBaseType();
  public void setBaseType(String baseType);
  
  public String getPresetLanguageCode();
  public void setPresetLanguageCode(String presetLanguageCode);
}
