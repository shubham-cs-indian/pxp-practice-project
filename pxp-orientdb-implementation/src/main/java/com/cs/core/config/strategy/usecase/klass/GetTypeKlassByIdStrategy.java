package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetTypeKlassByIdStrategy extends OrientDBBaseStrategy
    implements IGetTypeKlassByIdStrategy {
  
  public static final String useCase = "GetTypeKlassById";
  
  @Override
  public IIdAndTypeModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(useCase, requestMap, IdAndTypeModel.class);
  }
}
