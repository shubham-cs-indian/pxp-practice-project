package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.interactor.model.tag.TagModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetAllMasterTagsStrategy extends OrientDBBaseStrategy
    implements IGetAllMasterTagsStrategy {
  
  @Override
  public IListModel<ITagModel> execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_ALL_MASTER_TAGS, model, new TypeReference<ListModel<TagModel>>()
    {
      
    });
  }
}
