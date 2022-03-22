package com.cs.core.config.businessapi.variantcontext;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.variantcontext.IGetAllVariantContextsResponseModel;
import com.cs.core.config.strategy.usecase.variantcontext.IGetAllVariantContextStrategy;
import coms.cs.core.config.businessapi.variantcontext.IGetAllVariantContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllVariantContextService extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetAllVariantContextsResponseModel>
  implements IGetAllVariantContextService {
  
  @Autowired
  protected IGetAllVariantContextStrategy getAllVariantContextStrategy;
  
  @Override
  public IGetAllVariantContextsResponseModel executeInternal(IConfigGetAllRequestModel variantContextModel)
      throws Exception
  {
    return getAllVariantContextStrategy.execute(variantContextModel);
  }
}
