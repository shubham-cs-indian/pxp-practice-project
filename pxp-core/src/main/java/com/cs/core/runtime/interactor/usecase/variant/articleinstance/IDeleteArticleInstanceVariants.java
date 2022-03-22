package com.cs.core.runtime.interactor.usecase.variant.articleinstance;

import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantsReturnModel;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IDeleteArticleInstanceVariants
    extends IRuntimeInteractor<IDeleteVariantModel, IBulkDeleteVariantsReturnModel> {
}
