package com.cs.core.runtime.interactor.usecase.klassinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.pim.runtime.articleinstance.IDeleteArticleInstanceTranslationService;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationResponseModel;
import com.cs.core.runtime.interactor.usecase.articleinstance.IDeleteArticleInstanceTranslation;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class DeleteArticleInstanceTranslation extends
    AbstractRuntimeInteractor<IDeleteTranslationRequestModel, IDeleteTranslationResponseModel>
    implements IDeleteArticleInstanceTranslation {
  
  @Autowired
  protected IDeleteArticleInstanceTranslationService deleteArticleInstanceTranslationService;

  @Override
  protected IDeleteTranslationResponseModel executeInternal(IDeleteTranslationRequestModel model) throws Exception
  {
    return deleteArticleInstanceTranslationService.execute(model);
  }
}







