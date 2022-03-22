package com.cs.core.config.interactor.usecase.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigDetailsToFetchDataForSmartDocumentResponseModel extends IModel {
  
  public static final String PRESET_CONFIG_DETAILS = "presetConfigDetails";
  
  public IGetSmartDocumentPresetModel getPresetConfigDetails();
  
  public void setPresetConfigDetails(IGetSmartDocumentPresetModel presetConfigDetails);
}
