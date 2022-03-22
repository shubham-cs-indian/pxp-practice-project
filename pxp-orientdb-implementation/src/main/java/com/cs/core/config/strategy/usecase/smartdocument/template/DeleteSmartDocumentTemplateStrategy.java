package com.cs.core.config.strategy.usecase.smartdocument.template;

import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class DeleteSmartDocumentTemplateStrategy extends OrientDBBaseStrategy
    implements IDeleteSmartDocumentTemplateStrategy {
  
  @Override
  public IBulkDeleteReturnModel execute(IIdParameterModel model) throws Exception
  {
    return execute(DELETE_SMART_DOCUMENT_TEMPLATE, model, BulkDeleteReturnModel.class);
  }
}
