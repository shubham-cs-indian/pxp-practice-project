package com.cs.core.config.pdfreactorserver;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.config.interactor.model.smartdocument.ISmartDocumentModel;

public interface ICheckAndSavePdfReactorServerConfigurationService extends ISaveConfigService<ISmartDocumentModel, IGetSmartDocumentWithTemplateModel> {
  
}
