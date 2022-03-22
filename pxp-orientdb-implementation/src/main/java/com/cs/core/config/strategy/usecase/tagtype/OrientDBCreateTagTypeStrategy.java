package com.cs.core.config.strategy.usecase.tagtype;

import com.cs.core.config.interactor.model.tag.ITagTypeModel;
import com.cs.core.config.interactor.model.tag.TagTypeModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBCreateTagTypeStrategy extends OrientDBBaseStrategy
    implements ICreateTagTypeStrategy {
  
  public static final String useCase = "CreateTagTypes";
  
  @Override
  public ITagTypeModel execute(ITagTypeModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("tagType", model);
    return execute(useCase, requestMap, TagTypeModel.class);
  }
}
