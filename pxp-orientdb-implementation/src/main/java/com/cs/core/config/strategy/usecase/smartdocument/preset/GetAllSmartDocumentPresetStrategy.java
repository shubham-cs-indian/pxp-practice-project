package com.cs.core.config.strategy.usecase.smartdocument.preset;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.smartdocument.preset.GetAllSmartDocumentPresetResponseModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetAllSmartDocumentPresetResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetAllSmartDocumentPresetStrategy extends OrientDBBaseStrategy
    implements IGetAllSmartDocumentPresetStrategy {
  
  @Override
  public IGetAllSmartDocumentPresetResponseModel execute(IConfigGetAllRequestModel model)
      throws Exception
  {
    return execute(GET_ALL_SMART_DOCUMENT_PRESET, model,
        new TypeReference<GetAllSmartDocumentPresetResponseModel>()
        {
          
        });
  }
}
