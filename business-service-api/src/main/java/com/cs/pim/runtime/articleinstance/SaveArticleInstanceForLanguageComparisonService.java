package com.cs.pim.runtime.articleinstance;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.klassinstance.IArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

@Service
public class SaveArticleInstanceForLanguageComparisonService extends SaveArticleInstanceForTabsService
    implements ISaveArticleInstanceForLanguageComparisonService {
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IArticleInstanceSaveModel klassInstancesModel)
      throws Exception
  {
    IGetKlassInstanceModel response = null;
    try {
      response = super.executeInternal(klassInstancesModel);
    }
    catch (KlassNotFoundException e) {
      throw new ArticleKlassNotFoundException(e);
    }
    return response;
  }
  
}