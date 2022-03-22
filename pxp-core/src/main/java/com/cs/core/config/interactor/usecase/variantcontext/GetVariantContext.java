package com.cs.core.config.interactor.usecase.variantcontext;

import coms.cs.core.config.businessapi.variantcontext.IGetVariantContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.config.strategy.usecase.variantcontext.IGetVariantContextStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetVariantContext
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetVariantContextModel>
    implements IGetVariantContext {
  
  @Autowired
  IGetVariantContextService getVariantContextService;
  
  @Override
  public IGetVariantContextModel executeInternal(IIdParameterModel variantContextModel) throws Exception
  {
    
    return getVariantContextService.execute(variantContextModel);
  }
}
