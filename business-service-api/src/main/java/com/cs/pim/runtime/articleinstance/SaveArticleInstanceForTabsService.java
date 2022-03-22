package com.cs.pim.runtime.articleinstance;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.klassinstance.IArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.klassinstance.AbstractSaveInstanceForTabs;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;

@Service
public class SaveArticleInstanceForTabsService
    extends AbstractSaveInstanceForTabs<IArticleInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveArticleInstanceForTabsService {
  
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
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.SAVEARTIKLE;
  }
}
