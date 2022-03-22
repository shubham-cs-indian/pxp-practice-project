package com.cs.core.runtime.strategy.usecase.configdetails;

import com.cs.core.config.interactor.model.configdetails.GetPostConfigDetailsResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component("postConfigDetailsForGetContextualTabStrategy")
public class PostConfigDetailsForGetContextualTabStrategy extends OrientDBBaseStrategy
    implements IGetPostConfigDetailsStrategy {
  
  public static final String useCase = "PostConfigDetailsForGetContextualTab";
  
  @Override
  public IGetPostConfigDetailsResponseModel execute(IGetPostConfigDetailsRequestModel model)
      throws Exception
  {
    return execute(useCase, model, GetPostConfigDetailsResponseModel.class);
  }
}
