package com.cs.core.runtime.variant.articleinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.model.variants.IBulkSaveKlassInstanceVariantsResponseModel;

public interface IBulkSaveArticleInstanceVariantsService extends
    IRuntimeService<IBulkSaveInstanceVariantsModel, IBulkSaveKlassInstanceVariantsResponseModel> {
}
