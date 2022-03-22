package com.cs.core.config.strategy.usecase.tagtype;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagTypeModel;
import com.cs.core.config.interactor.model.tag.TagTypeModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetAllTagTypesStrategy extends OrientDBBaseStrategy
    implements IGetAllTagTypesStrategy {
  
  public static final String useCase = "GetTagTypes";
  
  @Override
  public IListModel<ITagTypeModel> execute(ITagTypeModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    return execute(useCase, requestMap, new TypeReference<ListModel<TagTypeModel>>()
    {
      
    });
  }
}
