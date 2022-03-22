package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.relationship.RelationshipCodeIdModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipCodeIdModel;
import org.springframework.stereotype.Component;

@Component
public class OrientDBGetRootRelationshipByCodeStrategy extends OrientDBBaseStrategy
    implements IGetRootRelationshipByCodeStrategy {
  
  @Override
  public IRelationshipCodeIdModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(GET_ROOT_RELATIONSHIP_ID_BY_CODE, model, RelationshipCodeIdModel.class);
  }
}
