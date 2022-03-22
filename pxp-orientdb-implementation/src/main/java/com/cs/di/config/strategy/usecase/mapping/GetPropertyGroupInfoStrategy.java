package com.cs.di.config.strategy.usecase.mapping;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.mapping.GetPropertyGroupInfoResponseModel;
import com.cs.core.config.interactor.model.mapping.IGetPropertyGroupInfoRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.mapping.IGetPropertyGroupInfoStrategy;

@Component("getPropertyGroupInfoStrategy")
public class GetPropertyGroupInfoStrategy extends OrientDBBaseStrategy
    implements IGetPropertyGroupInfoStrategy {
  
  @Override
  public GetPropertyGroupInfoResponseModel execute(IGetPropertyGroupInfoRequestModel model)
      throws Exception
  {
    return execute(GET_ATTRIBUTE_TAG_INFO, model, GetPropertyGroupInfoResponseModel.class);
  }
}
