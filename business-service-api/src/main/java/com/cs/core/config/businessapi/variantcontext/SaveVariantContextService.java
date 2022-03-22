package com.cs.core.config.businessapi.variantcontext;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.ISaveVariantContextModel;
import com.cs.core.config.strategy.usecase.variantcontext.ISaveVariantContextStrategy;
import coms.cs.core.config.businessapi.variantcontext.ISaveVariantContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveVariantContextService extends AbstractSaveConfigService<ISaveVariantContextModel, IGetVariantContextModel>
  implements ISaveVariantContextService {
  
  @Autowired
  ISaveVariantContextStrategy saveVariantContextStrategy;
  
  @Override
  public IGetVariantContextModel executeInternal(ISaveVariantContextModel model) throws Exception
  {
    ContextValidations.validateContext(model);
    return saveVariantContextStrategy.execute(model);
  }
}
