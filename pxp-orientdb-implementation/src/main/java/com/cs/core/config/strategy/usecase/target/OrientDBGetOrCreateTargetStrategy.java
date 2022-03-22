package com.cs.core.config.strategy.usecase.target;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetOrCreateTargetStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateMarketStrategy {
  
  public static final String useCase = "GetOrCreateTarget";
  
  @Override
  public ITargetModel execute(IListModel<ITargetModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    execute(useCase, requestMap);
    /*ITarget savedTargetKlass = ObjectMapperUtil.readValue(response, ITarget.class);
    return new TargetModel((Target) savedTargetKlass);*/
    return null;
  }
}
