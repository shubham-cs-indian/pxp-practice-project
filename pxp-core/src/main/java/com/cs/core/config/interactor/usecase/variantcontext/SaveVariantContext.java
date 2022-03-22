package com.cs.core.config.interactor.usecase.variantcontext;

import coms.cs.core.config.businessapi.variantcontext.ISaveVariantContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.ISaveVariantContextModel;
import com.cs.core.config.strategy.usecase.variantcontext.ISaveVariantContextStrategy;

@Service
public class SaveVariantContext
    extends AbstractSaveConfigInteractor<ISaveVariantContextModel, IGetVariantContextModel>
    implements ISaveVariantContext {
  
  @Autowired
  ISaveVariantContextService saveVariantContextService;
  
  @Override
  public IGetVariantContextModel executeInternal(ISaveVariantContextModel model) throws Exception
  {
    return saveVariantContextService.execute(model);
  }
}
