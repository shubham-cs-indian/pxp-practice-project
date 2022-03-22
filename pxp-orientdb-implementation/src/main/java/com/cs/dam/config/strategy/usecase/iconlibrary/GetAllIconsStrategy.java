package com.cs.dam.config.strategy.usecase.iconlibrary;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.asset.GetAllIconsResponseModel;
import com.cs.core.config.interactor.model.asset.IGetAllIconsRequestModel;
import com.cs.core.config.interactor.model.asset.IGetAllIconsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component("getAllIconsStrategy")
public class GetAllIconsStrategy extends OrientDBBaseStrategy implements IGetAllIconsStrategy{

  @Override
  public IGetAllIconsResponseModel execute(IGetAllIconsRequestModel model) throws Exception
  {
    return execute(GET_ALL_ICONS, model, GetAllIconsResponseModel.class);
  }
  
}
