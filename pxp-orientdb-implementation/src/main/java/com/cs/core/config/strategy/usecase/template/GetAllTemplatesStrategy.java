package com.cs.core.config.strategy.usecase.template;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.template.GetGridTemplatesResponseModel;
import com.cs.core.config.interactor.model.template.IGetGridTemplatesResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetAllTemplatesStrategy extends OrientDBBaseStrategy
    implements IGetAllTemplatesStrategy {
  
  @Override
  public IGetGridTemplatesResponseModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(GET_ALL_TEMPLATES, model, GetGridTemplatesResponseModel.class);
  }
}
