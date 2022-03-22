package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.klass.GetKlassesForRelationshipModel;
import com.cs.core.config.interactor.model.klass.IGetKlassesForRelationshipModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassesForRelationshipSideStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetKlassesForRelationshipSideStrategy extends OrientDBBaseStrategy
    implements IGetKlassesForRelationshipSideStrategy {
  
  @Override
  public IGetKlassesForRelationshipModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(GET_KLASSES_FOR_RELATIONSHIP_SIDE, requestMap,
        GetKlassesForRelationshipModel.class);
  }
}
