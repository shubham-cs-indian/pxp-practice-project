package com.cs.core.config.smartdocument.template;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.smartdocument.template.IGetSmartDocumentTemplateWithPresetModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;

public interface ISaveSmartDocumentTemplateService
    extends ISaveConfigService<ISmartDocumentTemplateModel, IGetSmartDocumentTemplateWithPresetModel> {
  
}
