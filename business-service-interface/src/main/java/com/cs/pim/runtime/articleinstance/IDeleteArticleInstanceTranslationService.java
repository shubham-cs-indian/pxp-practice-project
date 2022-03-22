package com.cs.pim.runtime.articleinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationResponseModel;

public interface IDeleteArticleInstanceTranslationService
    extends IRuntimeService<IDeleteTranslationRequestModel, IDeleteTranslationResponseModel> {
  
}
