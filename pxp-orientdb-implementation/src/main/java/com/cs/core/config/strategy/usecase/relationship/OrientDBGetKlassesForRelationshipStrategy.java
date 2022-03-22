package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.klass.GetKlassesForRelationshipModel;
import com.cs.core.config.interactor.model.klass.IGetKlassesForRelationshipModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassesForRelationshipStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetKlassesForRelationshipStrategy extends OrientDBBaseStrategy
    implements IGetKlassesForRelationshipStrategy {
  
  @Override
  public IGetKlassesForRelationshipModel execute(IGetKlassesForRelationshipModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    return execute(GET_KLASSES_FOR_RELATIONSHIP, requestMap, GetKlassesForRelationshipModel.class);
  }
}
