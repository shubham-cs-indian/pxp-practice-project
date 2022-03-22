package com.cs.core.config.smartdocument.template;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.smartdocument.template.IGetSmartDocumentTemplateWithPresetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSmartDocumentTemplateService extends IGetConfigService<IIdParameterModel, IGetSmartDocumentTemplateWithPresetModel> {
  
}
