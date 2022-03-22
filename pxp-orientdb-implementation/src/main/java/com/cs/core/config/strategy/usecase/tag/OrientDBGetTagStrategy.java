package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.tag.IGetTagModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.interactor.model.tag.TagModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetTagStrategy extends OrientDBBaseStrategy implements IGetTagStrategy {
  
  @Override
  public ITagModel execute(IGetTagModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    requestMap.put("mode", model.getMode());
    
    return super.execute(OrientDBBaseStrategy.GET_TAG, requestMap, TagModel.class);
  }
}
