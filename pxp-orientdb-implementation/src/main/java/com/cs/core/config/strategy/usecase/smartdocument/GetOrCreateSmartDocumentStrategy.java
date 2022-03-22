package com.cs.core.config.strategy.usecase.smartdocument;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetOrCreateSmartDocumentStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateSmartDocumentStrategy {
  
  @Override
  public IModel execute(IModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("smartDocument", model);
    execute(GET_OR_CREATE_SMART_DOCUMENT, requestMap);
    return null;
  }
}
