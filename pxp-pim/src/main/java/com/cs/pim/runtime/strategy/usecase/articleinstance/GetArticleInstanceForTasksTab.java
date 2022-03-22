package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.articleinstance.IGetArticleInstanceForTasksTabService;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetArticleInstanceForTasksTab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GetArticleInstanceForTasksTab  extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetTaskInstanceResponseModel>
implements IGetArticleInstanceForTasksTab {

  @Autowired
  protected IGetArticleInstanceForTasksTabService getArticleInstanceForTasksTabService;

  @Override
  protected IGetTaskInstanceResponseModel executeInternal(
      IGetInstanceRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
      return getArticleInstanceForTasksTabService.execute(getKlassInstanceTreeStrategyModel);
  }


 
}
