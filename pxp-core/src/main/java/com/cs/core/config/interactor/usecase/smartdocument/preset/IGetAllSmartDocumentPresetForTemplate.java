package com.cs.core.config.interactor.usecase.smartdocument.preset;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllSmartDocumentPresetForTemplate
    extends IGetConfigInteractor<IIdParameterModel, IListModel<ISmartDocumentPresetModel>> {
  
}
