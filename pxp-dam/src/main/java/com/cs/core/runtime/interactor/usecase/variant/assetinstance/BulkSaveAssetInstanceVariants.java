package com.cs.core.runtime.interactor.usecase.variant.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.model.variants.IBulkSaveKlassInstanceVariantsResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.variant.assetinstance.IBulkSaveAssetInstanceVariantsService;

@Service
public class BulkSaveAssetInstanceVariants
    extends AbstractRuntimeInteractor<IBulkSaveInstanceVariantsModel, IBulkSaveKlassInstanceVariantsResponseModel>
    implements IBulkSaveAssetInstanceVariants {
  
  @Autowired
  protected IBulkSaveAssetInstanceVariantsService bulkSaveAssetInstanceVariantsService;
  
  @Override
  public IBulkSaveKlassInstanceVariantsResponseModel executeInternal(IBulkSaveInstanceVariantsModel dataModel) throws Exception
  {
    return bulkSaveAssetInstanceVariantsService.execute(dataModel);
  }
  
}
