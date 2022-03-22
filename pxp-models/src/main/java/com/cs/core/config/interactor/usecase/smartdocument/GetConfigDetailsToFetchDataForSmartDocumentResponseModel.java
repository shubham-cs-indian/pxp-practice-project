package com.cs.core.config.interactor.usecase.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.preset.GetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetConfigDetailsToFetchDataForSmartDocumentResponseModel
    implements IGetConfigDetailsToFetchDataForSmartDocumentResponseModel {
  
  private static final long              serialVersionUID = 1L;
  protected IGetSmartDocumentPresetModel presetConfigDetails;
  
  @Override
  public IGetSmartDocumentPresetModel getPresetConfigDetails()
  {
    return presetConfigDetails;
  }
  
  @Override
  @JsonDeserialize(as = GetSmartDocumentPresetModel.class)
  public void setPresetConfigDetails(IGetSmartDocumentPresetModel presetConfigDetails)
  {
    this.presetConfigDetails = presetConfigDetails;
  }
}
