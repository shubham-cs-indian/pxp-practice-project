package com.cs.core.config.smartdocument.template;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllSmartDocumentTemplateService extends IGetConfigService<IIdParameterModel, IListModel<ISmartDocumentTemplateModel>> {
  
}
