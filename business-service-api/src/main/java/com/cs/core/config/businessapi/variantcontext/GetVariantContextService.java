package com.cs.core.config.businessapi.variantcontext;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.config.strategy.usecase.variantcontext.IGetVariantContextStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import coms.cs.core.config.businessapi.variantcontext.IGetVariantContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetVariantContextService extends AbstractGetConfigService<IIdParameterModel, IGetVariantContextModel>
  implements IGetVariantContextService {
  
  @Autowired
  IGetVariantContextStrategy getVariantContextStrategy;
  
  @Override
  public IGetVariantContextModel executeInternal(IIdParameterModel variantContextModel) throws Exception
  {
    
    return getVariantContextStrategy.execute(variantContextModel);
  }
}
