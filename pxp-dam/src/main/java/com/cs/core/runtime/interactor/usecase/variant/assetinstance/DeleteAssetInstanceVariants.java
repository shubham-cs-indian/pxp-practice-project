package com.cs.core.runtime.interactor.usecase.variant.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantsReturnModel;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.variant.assetinstance.IDeleteAssetInstanceVariantsService;

@Service("deleteTechnicalImageVariant")
public class DeleteAssetInstanceVariants extends AbstractRuntimeInteractor<IDeleteVariantModel, IBulkDeleteVariantsReturnModel>
    implements IDeleteAssetInstanceVariants {
  
  @Autowired
  protected IDeleteAssetInstanceVariantsService deleteAssetInstanceVariantsService;
  
  @Override
  protected IBulkDeleteVariantsReturnModel executeInternal(IDeleteVariantModel model) throws Exception
  {
    return deleteAssetInstanceVariantsService.execute(model);
  }
}
