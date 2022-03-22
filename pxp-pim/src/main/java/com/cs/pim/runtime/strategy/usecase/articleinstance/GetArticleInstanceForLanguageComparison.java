package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceForLanguageComparisonRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.ILanguageComparisonResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.articleinstance.IGetArticleInstanceForLanguageComparisonService;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetArticleInstanceForLanguageComparison;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetArticleInstanceForLanguageComparison extends AbstractRuntimeInteractor<IGetInstanceForLanguageComparisonRequestModel, ILanguageComparisonResponseModel>
    implements IGetArticleInstanceForLanguageComparison {

  @Autowired
  protected IGetArticleInstanceForLanguageComparisonService getArticleInstanceForLanguageComparisonService;
  
  @Override
  protected ILanguageComparisonResponseModel executeInternal(
      IGetInstanceForLanguageComparisonRequestModel klassInstancesModel) throws Exception
  {
      return getArticleInstanceForLanguageComparisonService.execute(klassInstancesModel);
  }
  
}