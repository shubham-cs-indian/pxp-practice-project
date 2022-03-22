package com.cs.core.config.strategy.usecase.smartdocument.template;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.config.interactor.model.smartdocument.template.SmartDocumentTemplateModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetAllSmartDocumentTemplateStrategy extends OrientDBBaseStrategy
    implements IGetAllSmartDocumentTemplateStrategy {
  
  @Override
  public IListModel<ISmartDocumentTemplateModel> execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_ALL_SMART_DOCUMENT_TEMPLATE, model,
        new TypeReference<ListModel<SmartDocumentTemplateModel>>()
        {
          
        });
  }
}
