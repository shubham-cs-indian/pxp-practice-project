package com.cs.core.config.strategy.usecase.smartdocument.template;

import com.cs.core.config.interactor.model.smartdocument.template.GetSmartDocumentTemplateWithPresetModel;
import com.cs.core.config.interactor.model.smartdocument.template.IGetSmartDocumentTemplateWithPresetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetSmartDocumentTemplateStrategy extends OrientDBBaseStrategy
    implements IGetSmartDocumentTemplateStrategy {
  
  @Override
  public IGetSmartDocumentTemplateWithPresetModel execute(IIdParameterModel dataModel)
      throws Exception
  {
    return execute(GET_SMART_DOCUMENT_TEMPLATE, dataModel,
        GetSmartDocumentTemplateWithPresetModel.class);
  }
}
