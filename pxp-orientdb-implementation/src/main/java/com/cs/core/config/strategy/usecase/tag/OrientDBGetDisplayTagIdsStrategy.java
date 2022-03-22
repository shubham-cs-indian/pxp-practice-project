package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component("getDisplayTagIdsStrategy")
public class OrientDBGetDisplayTagIdsStrategy extends OrientDBBaseStrategy
    implements IGetDisplayTagIdsStrategy {
  
  @Override
  public IListModel<String> execute(ITagModel model) throws Exception
  {
    List<String> dimensionalTagIds = execute(GET_ALL_DISPLAY_TAG_IDS, new HashMap<String, Object>(),
        new TypeReference<List<String>>()
        {
          
        });
    IListModel<String> responseListModel = new ListModel<>();
    responseListModel.setList(dimensionalTagIds);
    return responseListModel;
  }
}
