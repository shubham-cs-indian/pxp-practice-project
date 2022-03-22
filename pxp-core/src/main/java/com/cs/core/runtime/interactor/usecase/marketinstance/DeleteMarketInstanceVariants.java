package com.cs.core.runtime.interactor.usecase.marketinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantsReturnModel;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.variant.marketinstance.IDeleteMarketInstanceVariants;
import com.cs.core.runtime.variant.marketinstance.IDeleteMarketInstanceVariantsService;

@Service
public class DeleteMarketInstanceVariants extends AbstractRuntimeInteractor<IDeleteVariantModel, IBulkDeleteVariantsReturnModel>
    implements IDeleteMarketInstanceVariants {
  
  @Autowired
  protected IDeleteMarketInstanceVariantsService deleteMarketInstanceVariantsService;
  
  @Override
  protected IBulkDeleteVariantsReturnModel executeInternal(IDeleteVariantModel model) throws Exception
  {
    return deleteMarketInstanceVariantsService.execute(model);
  }
}
