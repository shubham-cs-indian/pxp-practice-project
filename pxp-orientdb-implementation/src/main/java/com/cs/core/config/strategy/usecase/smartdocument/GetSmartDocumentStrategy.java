package com.cs.core.config.strategy.usecase.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.GetSmartDocumentWithTemplateModel;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component("getSmartDocumentStrategy")
public class GetSmartDocumentStrategy extends OrientDBBaseStrategy
    implements IGetSmartDocumentStrategy {
  
  @Override
  public IGetSmartDocumentWithTemplateModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_SMART_DOCUMENT, model, GetSmartDocumentWithTemplateModel.class);
  }
}
