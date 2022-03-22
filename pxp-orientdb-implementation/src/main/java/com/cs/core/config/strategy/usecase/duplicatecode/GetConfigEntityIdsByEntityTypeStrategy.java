package com.cs.core.config.strategy.usecase.duplicatecode;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IGetEntityIdsByEntityTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetConfigEntityIdsByEntityTypeStrategy extends OrientDBBaseStrategy
    implements IGetConfigEntityIdsByEntityTypeStrategy {
  
  @Override
  public IIdsListParameterModel execute(IGetEntityIdsByEntityTypeModel dataModel) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_CONFIG_ENTITY_IDS_BY_ENTITY_TYPE, dataModel,
        IdsListParameterModel.class);
  }
}
