package com.cs.core.config.strategy.usecase.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.GetSmartDocumentWithTemplateModel;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.config.interactor.model.smartdocument.ISmartDocumentModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class SaveSmartDocumentStrategy extends OrientDBBaseStrategy
    implements ISaveSmartDocumentStrategy {
  
  @Override
  public IGetSmartDocumentWithTemplateModel execute(ISmartDocumentModel model) throws Exception
  {
    return execute(SAVE_SMART_DOCUMENT, model, GetSmartDocumentWithTemplateModel.class);
  }
}
