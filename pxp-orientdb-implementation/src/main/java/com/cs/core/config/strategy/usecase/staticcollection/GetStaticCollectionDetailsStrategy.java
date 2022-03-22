package com.cs.core.config.strategy.usecase.staticcollection;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.collections.GetStaticCollectionInfoModel;
import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetStaticCollectionDetailsStrategy extends OrientDBBaseStrategy
    implements IGetStaticCollectionDetailsStrategy {
  
  @Override
  public IGetStaticCollectionInfoModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_COLLECTION_DETAILS, model, GetStaticCollectionInfoModel.class);
  }
}
