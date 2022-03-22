package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.offboarding.IKlassRelationshipSidesInfoModel;
import com.cs.core.runtime.interactor.model.offboarding.KlassRelationshipSidesInfoModel;
import org.springframework.stereotype.Component;

@Component
public class OrientDBGetRootRelationshipStrategy extends OrientDBBaseStrategy
    implements IGetRootRelationshipStrategy {
  
  @Override
  public IKlassRelationshipSidesInfoModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_ROOT_RELATIONSHIP, model, KlassRelationshipSidesInfoModel.class);
  }
}
