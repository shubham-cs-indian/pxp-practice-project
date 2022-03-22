package com.cs.core.config.smartdocument;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSmartDocumentService extends IGetConfigService<IIdParameterModel, IGetSmartDocumentWithTemplateModel> {
  
}
