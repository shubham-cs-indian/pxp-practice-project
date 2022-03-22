package com.cs.core.config.businessapi.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.usecase.tag.IGetAllTagsStrategy;

@Service
public class GetAllTagsService extends AbstractGetConfigService<ITagModel, IListModel<ITagModel>>
    implements IGetAllTagsService {
  
  @Autowired
  IGetAllTagsStrategy neo4jGetAllTagsStrategy;
  
  @Override
  public IListModel<ITagModel> executeInternal(ITagModel dataModel) throws Exception
  {
    return neo4jGetAllTagsStrategy.execute(dataModel);
  }

}
