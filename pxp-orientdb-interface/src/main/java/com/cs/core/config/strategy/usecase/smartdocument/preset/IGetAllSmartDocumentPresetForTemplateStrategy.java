package com.cs.core.config.strategy.usecase.smartdocument.preset;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllSmartDocumentPresetForTemplateStrategy
    extends IConfigStrategy<IIdParameterModel, IListModel<ISmartDocumentPresetModel>> {
  
}
