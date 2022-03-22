package com.cs.core.config.strategy.usecase.smartdocument.preset;

import com.cs.core.config.interactor.model.smartdocument.preset.GetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getSmartDocumentPresetStrategy")
public class GetSmartDocumentPresetStrategy extends OrientDBBaseStrategy
    implements IGetSmartDocumentPresetStrategy {
  
  @Override
  public IGetSmartDocumentPresetModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(GET_SMART_DOCUMENT_PRESET, requestMap, GetSmartDocumentPresetModel.class);
  }
}
