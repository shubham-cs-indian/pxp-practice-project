package com.cs.core.runtime.interactor.usecase.variant.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.model.variants.IBulkSaveKlassInstanceVariantsResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.textassetinstance.IBulkSaveTextAssetInstanceVariants;
import com.cs.core.runtime.variant.textassetinstance.IBulkSaveTextAssetInstanceVariantsService;

@Service
public class BulkSaveTextAssetInstanceVariants
    extends AbstractRuntimeInteractor<IBulkSaveInstanceVariantsModel, IBulkSaveKlassInstanceVariantsResponseModel>
    implements IBulkSaveTextAssetInstanceVariants {
  
  @Autowired
  protected IBulkSaveTextAssetInstanceVariantsService bulkSaveTextAssetInstanceVariantsService;
  
  @Override
  public IBulkSaveKlassInstanceVariantsResponseModel executeInternal(IBulkSaveInstanceVariantsModel dataModel) throws Exception
  {
    return bulkSaveTextAssetInstanceVariantsService.execute(dataModel);
  }
  
}
