package com.cs.core.config.strategy.usecase.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.user.IUserLanguageModel;
import com.cs.core.config.interactor.model.user.UserLanguageModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Component("getLanguageForUserStrategy")
public class OrientDBGetLanguageForUserStrategy extends OrientDBBaseStrategy implements IGetLanguageForUserStrategy {
  
  
  @Override
  public IUserLanguageModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(GET_LANGUAGE_FOR_USER, requestMap, UserLanguageModel.class);
  }
}
