package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.interactor.model.tag.TagModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetAllTagsStrategy extends OrientDBBaseStrategy
    implements IGetAllTagsStrategy {
  
  public static final String useCase = "GetAllTags";
  
  @Override
  public IListModel<ITagModel> execute(ITagModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    return execute(useCase, requestMap, new TypeReference<ListModel<TagModel>>()
    {
      
    });
  }
}
