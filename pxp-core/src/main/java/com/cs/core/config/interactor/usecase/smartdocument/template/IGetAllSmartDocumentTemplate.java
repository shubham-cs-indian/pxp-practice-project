package com.cs.core.config.interactor.usecase.smartdocument.template;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllSmartDocumentTemplate
    extends IGetConfigInteractor<IIdParameterModel, IListModel<ISmartDocumentTemplateModel>> {
  
}
