package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.articleinstance.IGetArticleInstanceForOverviewTab;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.articleinstance.IGetArticleInstanceForOverviewTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetArticleInstanceForOverviewTab extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel>
    implements IGetArticleInstanceForOverviewTab {
  
  @Autowired
  protected IGetArticleInstanceForOverviewTabService getArticleInstanceForOverviewTabService;
  
  @Override
  protected IGetKlassInstanceCustomTabModel executeInternal(
      IGetInstanceRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
      return getArticleInstanceForOverviewTabService.execute(getKlassInstanceTreeStrategyModel);
  }

}
