package com.cs.core.config.iconlibrary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.asset.IGetAllIconsRequestModel;
import com.cs.core.config.interactor.model.asset.IGetAllIconsResponseModel;
import com.cs.dam.config.strategy.usecase.iconlibrary.IGetAllIconsStrategy;

@Service
public class GetAllIconsService extends AbstractGetConfigService<IGetAllIconsRequestModel, IGetAllIconsResponseModel>
    implements IGetAllIconsService {
  
  @Autowired
  protected IGetAllIconsStrategy getAllIconsStrategy;
  
  @Override
  protected IGetAllIconsResponseModel executeInternal(IGetAllIconsRequestModel model) throws Exception
  {
    return getAllIconsStrategy.execute(model);
  }
}
