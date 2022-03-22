package com.cs.core.config.strategy.usecase.smartdocument.preset;

import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateSmartDocumentPresetStrategy
    extends IConfigStrategy<ISmartDocumentPresetModel, IGetSmartDocumentPresetModel> {
  
}
