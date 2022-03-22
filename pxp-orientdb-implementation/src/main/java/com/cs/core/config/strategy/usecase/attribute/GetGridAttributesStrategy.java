package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.model.attribute.IGetGridAttributesResponseModel;
import com.cs.core.config.interactor.model.attribute.standard.GetGridAttributesResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetGridAttributesStrategy extends OrientDBBaseStrategy
    implements IGetGridAttributesStrategy {
  
  @Override
  public IGetGridAttributesResponseModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.GET_GRID_ATTRIBUTES, model,
        GetGridAttributesResponseModel.class);
  }
}
