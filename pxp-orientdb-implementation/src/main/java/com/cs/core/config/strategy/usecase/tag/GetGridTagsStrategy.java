package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.tag.GetTagGridResponseModel;
import com.cs.core.config.interactor.model.tag.IGetTagGridResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetGridTagsStrategy extends OrientDBBaseStrategy implements IGetGridTagsStrategy {
  
  @Override
  public IGetTagGridResponseModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(GET_GRID_TAGS, model, GetTagGridResponseModel.class);
  }
}
