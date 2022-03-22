package com.cs.core.config.strategy.usecase.staticcollection;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetStaticCollectionHierarchyStrategy extends OrientDBBaseStrategy
    implements IGetStaticCollectionHierarchyStrategy {
  
  @Override
  public IIdsListParameterModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_COLLECTION_HIERARCHY, model, IdsListParameterModel.class);
  }
}
