package com.cs.pim.runtime.strategy.usecase.textassetinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.textassetinstance.IGetTextAssetInstanceForTasksTab;
import com.cs.pim.runtime.textassetinstance.IGetTextAssetInstanceForTasksTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTextAssetInstanceForTasksTab extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetTaskInstanceResponseModel>
implements IGetTextAssetInstanceForTasksTab    {


  @Autowired
  protected IGetTextAssetInstanceForTasksTabService getTextAssetInstanceForTasksTabService;

  @Override
  protected IGetTaskInstanceResponseModel executeInternal(
      IGetInstanceRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
      return getTextAssetInstanceForTasksTabService.execute(getKlassInstanceTreeStrategyModel);
  }
}
