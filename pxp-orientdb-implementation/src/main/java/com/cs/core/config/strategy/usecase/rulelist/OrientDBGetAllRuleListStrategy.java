package com.cs.core.config.strategy.usecase.rulelist;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.rulelist.GetAllRuleListsResponseModel;
import com.cs.core.config.interactor.model.rulelist.IGetAllRuleListsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class OrientDBGetAllRuleListStrategy extends OrientDBBaseStrategy
    implements IGetAllRuleListStrategy {
  
  @Override
  public IGetAllRuleListsResponseModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    
    return execute(GET_ALL_RULE_LIST, model, GetAllRuleListsResponseModel.class);
  }
}
