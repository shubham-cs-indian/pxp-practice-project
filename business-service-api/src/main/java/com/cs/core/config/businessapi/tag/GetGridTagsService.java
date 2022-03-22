package com.cs.core.config.businessapi.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.tag.IGetTagGridResponseModel;
import com.cs.core.config.strategy.usecase.tag.IGetGridTagsStrategy;

@Service
public class GetGridTagsService extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetTagGridResponseModel>
    implements IGetGridTagsService {
  
  @Autowired
  protected IGetGridTagsStrategy getGridTagsStrategy;
  
  @Override
  public IGetTagGridResponseModel executeInternal(IConfigGetAllRequestModel model) throws Exception
  {
    return getGridTagsStrategy.execute(model);
  }
}
