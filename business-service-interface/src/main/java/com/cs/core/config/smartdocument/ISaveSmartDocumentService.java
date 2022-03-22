package com.cs.core.config.smartdocument;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.config.interactor.model.smartdocument.ISmartDocumentModel;

public interface ISaveSmartDocumentService extends ISaveConfigService<ISmartDocumentModel, IGetSmartDocumentWithTemplateModel> {
  
}
