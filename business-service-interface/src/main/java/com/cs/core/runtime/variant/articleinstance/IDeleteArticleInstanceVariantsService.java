package com.cs.core.runtime.variant.articleinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantsReturnModel;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantModel;

public interface IDeleteArticleInstanceVariantsService extends IRuntimeService<IDeleteVariantModel, IBulkDeleteVariantsReturnModel> {
}
