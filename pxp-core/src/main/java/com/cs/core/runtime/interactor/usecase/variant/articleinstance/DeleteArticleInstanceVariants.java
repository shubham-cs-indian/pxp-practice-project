package com.cs.core.runtime.interactor.usecase.variant.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantsReturnModel;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.variant.articleinstance.IDeleteArticleInstanceVariantsService;

@Service
public class DeleteArticleInstanceVariants extends AbstractRuntimeInteractor<IDeleteVariantModel, IBulkDeleteVariantsReturnModel>
    implements IDeleteArticleInstanceVariants {
  
  @Autowired
  protected IDeleteArticleInstanceVariantsService deleteArticleInstanceVariantsService;
  
  @Override
  protected IBulkDeleteVariantsReturnModel executeInternal(IDeleteVariantModel model) throws Exception
  {
    return deleteArticleInstanceVariantsService.execute(model);
  }
}
