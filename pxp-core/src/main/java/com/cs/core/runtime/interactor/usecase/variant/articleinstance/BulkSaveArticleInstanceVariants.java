package com.cs.core.runtime.interactor.usecase.variant.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.model.variants.IBulkSaveKlassInstanceVariantsResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.variant.articleinstance.IBulkSaveArticleInstanceVariantsService;

@Service
public class BulkSaveArticleInstanceVariants
    extends AbstractRuntimeInteractor<IBulkSaveInstanceVariantsModel, IBulkSaveKlassInstanceVariantsResponseModel>
    implements IBulkSaveArticleInstanceVariants {
  
  @Autowired
  protected IBulkSaveArticleInstanceVariantsService bulkSaveArticleInstanceVariantsService;
  
  @Override
  public IBulkSaveKlassInstanceVariantsResponseModel executeInternal(IBulkSaveInstanceVariantsModel dataModel) throws Exception
  {
    return bulkSaveArticleInstanceVariantsService.execute(dataModel);
  }
  
}
