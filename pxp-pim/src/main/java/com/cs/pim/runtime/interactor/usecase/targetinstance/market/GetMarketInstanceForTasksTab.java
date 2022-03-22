package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.targetinstance.market.IGetMarketInstanceForTasksTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetMarketInstanceForTasksTab
    extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetTaskInstanceResponseModel>
    implements IGetMarketInstanceForTasksTab {

  @Autowired
  protected IGetMarketInstanceForTasksTabService getMarketInstanceForTasksTabService;

  @Override
  protected IGetTaskInstanceResponseModel executeInternal(
      IGetInstanceRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
      return getMarketInstanceForTasksTabService.execute(getKlassInstanceTreeStrategyModel);
  }
}
