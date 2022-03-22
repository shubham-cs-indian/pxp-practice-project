package com.cs.core.config.strategy.usecase.datarule;

import com.cs.core.config.interactor.model.datarule.GetAllDataRulesResponseModel;
import com.cs.core.config.interactor.model.datarule.IGetAllDataRulesResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class OrientDBGetDataRulesStrategy extends OrientDBBaseStrategy
    implements IGetAllDataRulesStrategy {
  
  @Override
  public IGetAllDataRulesResponseModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_DATA_RULES, model, GetAllDataRulesResponseModel.class);
  }
}
