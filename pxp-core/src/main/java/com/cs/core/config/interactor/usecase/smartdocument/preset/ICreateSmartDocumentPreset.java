package com.cs.core.config.interactor.usecase.smartdocument.preset;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;

public interface ICreateSmartDocumentPreset
    extends ICreateConfigInteractor<ISmartDocumentPresetModel, IGetSmartDocumentPresetModel> {
  
}
