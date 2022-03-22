package com.cs.core.config.interactor.usecase.duplicatecode;

import com.cs.core.config.strategy.usecase.duplicatecode.IGetConfigEntityIdsByEntityTypeStrategy;
import com.cs.core.runtime.interactor.model.configuration.IGetEntityIdsByEntityTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetConfigEntityIdsByEntityType extends
    AbstractGetConfigEntityIdsByEntityType<IGetEntityIdsByEntityTypeModel, IIdsListParameterModel>
    implements IGetConfigEntityIdsbyEntityType {
  
  @Autowired
  IGetConfigEntityIdsByEntityTypeStrategy getConfigEntityIdsByEntityTypeStrategy;
  
  @Override
  protected IIdsListParameterModel getConfigEntityIds(IGetEntityIdsByEntityTypeModel model)
      throws Exception
  {
    return getConfigEntityIdsByEntityTypeStrategy.execute(model);
  }
}
