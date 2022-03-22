package com.cs.core.config.interactor.usecase.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.tag.IGetGridTagsService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.tag.IGetTagGridResponseModel;

@Service
public class GetGridTags
    extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetTagGridResponseModel>
    implements IGetGridTags {
  
  @Autowired
  protected IGetGridTagsService getGridTagsService;
  
  @Override
  public IGetTagGridResponseModel executeInternal(IConfigGetAllRequestModel model) throws Exception
  {
    return getGridTagsService.execute(model);
  }
}

