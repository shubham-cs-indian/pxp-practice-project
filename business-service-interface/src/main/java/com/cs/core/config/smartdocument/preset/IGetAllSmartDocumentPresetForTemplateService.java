package com.cs.core.config.smartdocument.preset;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllSmartDocumentPresetForTemplateService
    extends IGetConfigService<IIdParameterModel, IListModel<ISmartDocumentPresetModel>> {
  
}
