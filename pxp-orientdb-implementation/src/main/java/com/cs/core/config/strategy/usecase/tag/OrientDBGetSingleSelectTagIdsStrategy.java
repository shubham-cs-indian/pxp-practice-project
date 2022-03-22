package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component("getSingleSelectTagIdsStrategy")
public class OrientDBGetSingleSelectTagIdsStrategy extends OrientDBBaseStrategy
    implements IGetSingleSelectTagIdsStrategy {
  
  public static final String useCase = "GetAllSingleSelectTagIds";
  
  @Override
  public IListModel<String> execute(ITagModel model) throws Exception
  {
    List<String> singleSelectTagIds = execute(useCase, new HashMap<String, Object>(),
        new TypeReference<List<String>>()
        {
          
        });
    IListModel<String> responseListModel = new ListModel<>();
    responseListModel.setList(singleSelectTagIds);
    return responseListModel;
  }
}
