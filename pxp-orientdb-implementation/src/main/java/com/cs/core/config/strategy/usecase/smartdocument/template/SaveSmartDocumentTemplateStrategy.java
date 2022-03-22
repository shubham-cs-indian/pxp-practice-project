package com.cs.core.config.strategy.usecase.smartdocument.template;

import com.cs.core.config.interactor.model.smartdocument.template.GetSmartDocumentTemplateWithPresetModel;
import com.cs.core.config.interactor.model.smartdocument.template.IGetSmartDocumentTemplateWithPresetModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class SaveSmartDocumentTemplateStrategy extends OrientDBBaseStrategy
    implements ISaveSmartDocumentTemplateStrategy {
  
  @Override
  public IGetSmartDocumentTemplateWithPresetModel execute(ISmartDocumentTemplateModel model)
      throws Exception
  {
    return execute(SAVE_SMART_DOCUMENT_TEMPLATE, model,
        GetSmartDocumentTemplateWithPresetModel.class);
  }
}
