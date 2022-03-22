package com.cs.core.config.strategy.usecase.smartdocument.preset;

import com.cs.core.config.interactor.model.smartdocument.preset.GetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISaveSmartDocumentPresetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component("saveSmartDocumentPresetStrategy")
public class SaveSmartDocumentPresetStrategy extends OrientDBBaseStrategy
    implements ISaveSmartDocumentPresetStrategy {
  
  @Override
  public IGetSmartDocumentPresetModel execute(ISaveSmartDocumentPresetModel model) throws Exception
  {
    return execute(SAVE_SMART_DOCUMENT_PRESET, model, GetSmartDocumentPresetModel.class);
  }
}
