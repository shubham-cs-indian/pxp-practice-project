package com.cs.core.runtime.interactor.usecase.variant.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantsReturnModel;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.variant.textassetinstance.IDeleteTextAssetInstanceVariantsService;

@Service
public class DeleteTextAssetInstanceVariants extends AbstractRuntimeInteractor<IDeleteVariantModel, IBulkDeleteVariantsReturnModel>
    implements IDeleteTextAssetInstanceVariants {
  
  @Autowired
  protected IDeleteTextAssetInstanceVariantsService deleteTextAssetInstanceVariantsService;
  
  @Override
  protected IBulkDeleteVariantsReturnModel executeInternal(IDeleteVariantModel model) throws Exception
  {
    return deleteTextAssetInstanceVariantsService.execute(model);
  }
}
