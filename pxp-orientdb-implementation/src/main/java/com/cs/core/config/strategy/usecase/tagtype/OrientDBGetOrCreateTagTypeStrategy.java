package com.cs.core.config.strategy.usecase.tagtype;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagTypeModel;
import com.cs.core.config.interactor.model.tag.TagTypeModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetOrCreateTagTypeStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateTagTypeStrategy {
  
  public static final String useCase = "GetOrCreateTagTypes";
  
  @Override
  public ITagTypeModel execute(IListModel<ITagTypeModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("tagType", model);
    return execute(useCase, requestMap, TagTypeModel.class);
  }
}
