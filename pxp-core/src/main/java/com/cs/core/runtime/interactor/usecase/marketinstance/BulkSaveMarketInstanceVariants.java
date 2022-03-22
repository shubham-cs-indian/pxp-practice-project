package com.cs.core.runtime.interactor.usecase.marketinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.model.variants.IBulkSaveKlassInstanceVariantsResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.variant.targetinstance.market.IBulkSaveMarketInstanceVariants;
import com.cs.core.runtime.variant.marketinstance.IBulkSaveMarketInstanceVariantsService;

@Service
public class BulkSaveMarketInstanceVariants
    extends AbstractRuntimeInteractor<IBulkSaveInstanceVariantsModel, IBulkSaveKlassInstanceVariantsResponseModel>
    implements IBulkSaveMarketInstanceVariants {
  
  @Autowired
  IBulkSaveMarketInstanceVariantsService bulkSaveMarketInstanceVariantsService;
  
  @Override
  public IBulkSaveKlassInstanceVariantsResponseModel executeInternal(IBulkSaveInstanceVariantsModel dataModel) throws Exception
  {
    
    return bulkSaveMarketInstanceVariantsService.execute(dataModel);
  }
  
}
