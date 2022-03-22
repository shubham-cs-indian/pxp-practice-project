package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.tagtype.IGetOrCreateTagsStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetOrCreateTagsStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateTagsStrategy {
  
  @SuppressWarnings("unchecked")
  @Override
  public IListModel<ITagModel> execute(IListModel<ITagModel> model) throws Exception
  {
    Map<String, Object> requestList = new HashMap<>();
    requestList.put("list", model.getList());
    return execute(GET_OR_CREATE_TAGS, requestList, ListModel.class);
  }
}
