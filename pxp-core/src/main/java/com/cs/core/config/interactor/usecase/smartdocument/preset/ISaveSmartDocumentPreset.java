package com.cs.core.config.interactor.usecase.smartdocument.preset;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISaveSmartDocumentPresetModel;

public interface ISaveSmartDocumentPreset
    extends ISaveConfigInteractor<ISaveSmartDocumentPresetModel, IGetSmartDocumentPresetModel> {
  
}
