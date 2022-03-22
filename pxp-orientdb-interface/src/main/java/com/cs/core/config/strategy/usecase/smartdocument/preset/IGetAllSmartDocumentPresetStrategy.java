package com.cs.core.config.strategy.usecase.smartdocument.preset;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetAllSmartDocumentPresetResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllSmartDocumentPresetStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetAllSmartDocumentPresetResponseModel> {
  
}
