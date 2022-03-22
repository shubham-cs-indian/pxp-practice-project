package com.cs.core.config.businessapi.attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.attribute.IGetGridAttributesResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.usecase.attribute.IGetGridAttributesStrategy;

@Service
public class GetGridAttributesService extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetGridAttributesResponseModel>
    implements IGetGridAttributesService {
  
  @Autowired
  protected IGetGridAttributesStrategy getAllAttributesForGridStrategy;
  
  @Override
  public IGetGridAttributesResponseModel executeInternal(IConfigGetAllRequestModel model) throws Exception
  {
    return getAllAttributesForGridStrategy.execute(model);
  }
}
