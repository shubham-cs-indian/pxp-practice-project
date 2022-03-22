package com.cs.core.config.strategy.usecase.klass;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConifgDetailsForTypeSwitchOfInstanceStrategy;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@Component
public class GetConifgDetailsForTypeSwitchOfInstanceStrategy extends OrientDBBaseStrategy
    implements IGetConifgDetailsForTypeSwitchOfInstanceStrategy {
  
  public static final String useCase = "GetConfigDetailsForSwitchType";
  
  
  @SuppressWarnings("unchecked")
  @Override
  public IGetConfigDetailsForCustomTabModel execute(IConfigDetailsForSwitchTypeRequestModel model)
      throws Exception
  {
    Map<String, Object> requestMap = ObjectMapperUtil.convertValue(model, HashMap.class);
    return execute(useCase, requestMap, GetConfigDetailsForCustomTabModel.class);
  }
}
