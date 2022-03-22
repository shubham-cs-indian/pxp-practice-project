package com.cs.core.config.smartdocument.preset;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetAllSmartDocumentPresetResponseModel;

public interface IGetAllSmartDocumentPresetService
    extends IGetConfigService<IConfigGetAllRequestModel, IGetAllSmartDocumentPresetResponseModel> {
  
}
