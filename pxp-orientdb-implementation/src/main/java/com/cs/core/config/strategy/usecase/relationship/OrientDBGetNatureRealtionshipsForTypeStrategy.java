package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetNatureRelationshipsForTypeStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetNatureRealtionshipsForTypeStrategy extends OrientDBBaseStrategy
    implements IGetNatureRelationshipsForTypeStrategy {
  
  @Override
  public IIdsListParameterModel execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(GET_NATURE_RELATIONSHIP_BY_TYPE, requestMap, IdsListParameterModel.class);
  }
}
