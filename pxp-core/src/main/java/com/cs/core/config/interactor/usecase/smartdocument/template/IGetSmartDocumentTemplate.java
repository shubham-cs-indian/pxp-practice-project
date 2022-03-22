package com.cs.core.config.interactor.usecase.smartdocument.template;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.template.IGetSmartDocumentTemplateWithPresetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSmartDocumentTemplate
    extends IGetConfigInteractor<IIdParameterModel, IGetSmartDocumentTemplateWithPresetModel> {
  
}
