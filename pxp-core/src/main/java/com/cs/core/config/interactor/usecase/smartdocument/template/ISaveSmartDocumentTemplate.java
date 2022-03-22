package com.cs.core.config.interactor.usecase.smartdocument.template;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.template.IGetSmartDocumentTemplateWithPresetModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;

public interface ISaveSmartDocumentTemplate extends
    ISaveConfigInteractor<ISmartDocumentTemplateModel, IGetSmartDocumentTemplateWithPresetModel> {
  
}
