package com.cs.core.config.interactor.usecase.smartdocument.preset;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetAllSmartDocumentPresetResponseModel;

public interface IGetAllSmartDocumentPreset
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetAllSmartDocumentPresetResponseModel> {
  
}
