package com.cs.core.config.strategy.usecase.smartdocument.preset;

import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISaveSmartDocumentPresetModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveSmartDocumentPresetStrategy
    extends IConfigStrategy<ISaveSmartDocumentPresetModel, IGetSmartDocumentPresetModel> {
  
}
