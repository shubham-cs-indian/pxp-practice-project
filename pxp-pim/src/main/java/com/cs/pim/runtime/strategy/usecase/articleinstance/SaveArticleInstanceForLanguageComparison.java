package com.cs.pim.runtime.strategy.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.klassinstance.IArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.articleinstance.ISaveArticleInstanceForLanguageComparisonService;
import com.cs.pim.runtime.interactor.usecase.articleinstance.ISaveArticleInstanceForLanguageComparison;

@Service
public class SaveArticleInstanceForLanguageComparison extends AbstractRuntimeInteractor<IArticleInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveArticleInstanceForLanguageComparison {
  
  @Autowired
  ISaveArticleInstanceForLanguageComparisonService SaveArticleInstanceForLanguageComparisonService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IArticleInstanceSaveModel klassInstancesModel) throws Exception
  {
    
    return SaveArticleInstanceForLanguageComparisonService.execute(klassInstancesModel);
  }
  
}