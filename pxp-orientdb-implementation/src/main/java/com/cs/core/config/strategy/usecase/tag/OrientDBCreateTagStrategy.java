package com.cs.core.config.strategy.usecase.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.tag.CreateTagResponseModel;
import com.cs.core.config.interactor.model.tag.ICreateTagResponseModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class OrientDBCreateTagStrategy extends OrientDBBaseStrategy implements ICreateTagStrategy {
  
  @Override
  public ICreateTagResponseModel execute(ITagModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("tag", model);
    return super.execute(OrientDBBaseStrategy.CREATE_TAG, requestMap, CreateTagResponseModel.class);
  }
}
