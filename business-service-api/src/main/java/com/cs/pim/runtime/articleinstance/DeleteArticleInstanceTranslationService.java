package com.cs.pim.runtime.articleinstance;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationResponseModel;
import com.cs.core.runtime.translations.AbstractDeleteTranslationInstanceService;

@Service
public class DeleteArticleInstanceTranslationService
    extends AbstractDeleteTranslationInstanceService<IDeleteTranslationRequestModel, IDeleteTranslationResponseModel>
    implements IDeleteArticleInstanceTranslationService {
  
  @Override
  protected IDeleteTranslationResponseModel executeInternal(IDeleteTranslationRequestModel model) throws Exception
  {
    return super.executeInternal(model);
  }
}
