package com.cs.core.config.strategy.usecase.relationship;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetAllNatureRelationshipsIdsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;

@Component("getAllNatureRelationshipsIdsStrategy")
public class OrientDBGetAllNatureRealtionshipsIdsStrategy extends OrientDBBaseStrategy
    implements IGetAllNatureRelationshipsIdsStrategy {
  
  @Override
  public IIdsListParameterModel execute(IVoidModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    return execute(GET_ALL_NATURE_RELATIONSHIP_IDS, requestMap, IdsListParameterModel.class);
  }
}
