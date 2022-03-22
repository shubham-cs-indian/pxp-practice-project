package com.cs.core.config.strategy.usecase.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.interactor.model.tag.TagModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;

@Component("getTagsByTagIdsStrategy")
public class GetTagsByTagIdsStrategy extends OrientDBBaseStrategy
    implements IGetTagsByTagIdsStrategy {
  
  @Override
  public IListModel<ITagModel> execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(GET_TAGS_BY_IDS, requestMap, new TypeReference<ListModel<TagModel>>()
    {
      
    });
  }
}
