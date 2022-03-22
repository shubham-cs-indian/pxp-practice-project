package com.cs.core.config.strategy.usecase.tagtype;

import com.cs.core.config.interactor.model.tag.ITagTypeModel;
import com.cs.core.config.interactor.model.tag.TagTypeModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetTagTypeStrategy extends OrientDBBaseStrategy
    implements IGetTagTypeStrategy {
  
  public static final String useCase = "GetTagType";
  
  @Override
  public ITagTypeModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(useCase, requestMap, TagTypeModel.class);
  }
}
