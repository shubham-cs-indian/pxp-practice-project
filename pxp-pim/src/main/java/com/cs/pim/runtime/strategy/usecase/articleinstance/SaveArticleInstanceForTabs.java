package com.cs.pim.runtime.strategy.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.klassinstance.IArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.articleinstance.ISaveArticleInstanceForTabsService;

@Service
public class SaveArticleInstanceForTabs extends AbstractRuntimeInteractor<IArticleInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveArticleInstanceForTabs {
  
  @Autowired
  protected ISaveArticleInstanceForTabsService saveArticleInstanceForTabsService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IArticleInstanceSaveModel klassInstancesModel) throws Exception
  {
    return saveArticleInstanceForTabsService.execute(klassInstancesModel);
  }
  
}
