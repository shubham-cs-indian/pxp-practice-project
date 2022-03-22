package com.cs.core.config.businessapi.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.tag.IGetTagModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.usecase.tag.IGetTagStrategy;

@Service
public class GetTagService extends AbstractGetConfigService<IGetTagModel, ITagModel>
    implements IGetTagService {
  
  @Autowired
  IGetTagStrategy neo4jGetTagStrategy;
  
  @Override
  public ITagModel executeInternal(IGetTagModel dataModel) throws Exception
  {
    return neo4jGetTagStrategy.execute(dataModel);
  }
}
