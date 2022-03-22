package com.cs.core.config.interactor.usecase.attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.attribute.IGetGridAttributesService;
import com.cs.core.config.interactor.model.attribute.IGetGridAttributesResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.usecase.attribute.IGetGridAttributes;

@Service
public class GetGridAttributes
    extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetGridAttributesResponseModel>
    implements IGetGridAttributes {
  
  @Autowired
  protected IGetGridAttributesService getGridAttributesService;
  
  @Override
  public IGetGridAttributesResponseModel executeInternal(IConfigGetAllRequestModel model) throws Exception
  {
    return getGridAttributesService.execute(model);
  }
}
