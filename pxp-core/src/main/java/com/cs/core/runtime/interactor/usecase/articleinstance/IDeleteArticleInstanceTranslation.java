package com.cs.core.runtime.interactor.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IDeleteArticleInstanceTranslation
    extends IRuntimeInteractor<IDeleteTranslationRequestModel, IDeleteTranslationResponseModel> {
  
}
