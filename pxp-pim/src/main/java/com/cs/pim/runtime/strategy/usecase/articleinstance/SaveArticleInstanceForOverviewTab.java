package com.cs.pim.runtime.strategy.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.klassinstance.IArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.articleinstance.ISaveArticleInstanceForOverviewTab;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.articleinstance.ISaveArticleInstanceForOverviewTabService;

@Service
public class SaveArticleInstanceForOverviewTab extends AbstractRuntimeInteractor<IArticleInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveArticleInstanceForOverviewTab {
  
  @Autowired
  protected ISaveArticleInstanceForOverviewTabService saveArticleInstanceForOverviewTabService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IArticleInstanceSaveModel model) throws Exception
  {
    return saveArticleInstanceForOverviewTabService.execute(model);
  }
  
}
