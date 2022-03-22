package com.cs.core.config.interactor.usecase.iconlibrary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.iconlibrary.IGetAllIconsService;
import com.cs.core.config.interactor.model.asset.IGetAllIconsRequestModel;
import com.cs.core.config.interactor.model.asset.IGetAllIconsResponseModel;

@Service
public class GetAllIcons extends AbstractGetConfigInteractor<IGetAllIconsRequestModel, IGetAllIconsResponseModel> implements IGetAllIcons{

  @Autowired
  protected IGetAllIconsService getAllIconsService;
  
  @Override
  protected IGetAllIconsResponseModel executeInternal(IGetAllIconsRequestModel model) throws Exception
  {
    return getAllIconsService.execute(model);
  }
}
