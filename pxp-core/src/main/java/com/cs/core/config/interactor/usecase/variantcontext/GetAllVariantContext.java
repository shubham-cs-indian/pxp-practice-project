package com.cs.core.config.interactor.usecase.variantcontext;

import coms.cs.core.config.businessapi.variantcontext.IGetAllVariantContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.variantcontext.IGetAllVariantContextsResponseModel;
import com.cs.core.config.strategy.usecase.variantcontext.IGetAllVariantContextStrategy;

@Service
public class GetAllVariantContext extends
    AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetAllVariantContextsResponseModel>
    implements IGetAllVariantContext {
  
  @Autowired
  protected IGetAllVariantContextService getAllVariantContextService;
  
  @Override
  public IGetAllVariantContextsResponseModel executeInternal(IConfigGetAllRequestModel variantContextModel)
      throws Exception
  {
    return getAllVariantContextService.execute(variantContextModel);
  }
}
