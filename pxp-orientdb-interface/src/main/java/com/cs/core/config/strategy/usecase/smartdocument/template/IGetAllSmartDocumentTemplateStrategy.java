package com.cs.core.config.strategy.usecase.smartdocument.template;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllSmartDocumentTemplateStrategy
    extends IConfigStrategy<IIdParameterModel, IListModel<ISmartDocumentTemplateModel>> {
  
}
