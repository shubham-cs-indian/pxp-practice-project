package com.cs.core.config.strategy.usecase.smartdocument.preset;

import com.cs.core.config.interactor.model.smartdocument.preset.GetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component("createSmartDocumentPresetStrategy")
public class CreateSmartDocumentPresetStrategy extends OrientDBBaseStrategy
    implements ICreateSmartDocumentPresetStrategy {
  
  @Override
  public IGetSmartDocumentPresetModel execute(ISmartDocumentPresetModel model) throws Exception
  {
    return execute(CREATE_SMART_DOCUMENT_PRESET, model, GetSmartDocumentPresetModel.class);
  }
}
