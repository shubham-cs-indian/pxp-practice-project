package com.cs.dam.runtime.interactor.usecase.assetinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.IGetAssetInstanceForTasksTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAssetInstanceForTasksTab extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetTaskInstanceResponseModel>
    implements IGetAssetInstanceForTasksTab {

  @Autowired
  protected IGetAssetInstanceForTasksTabService getAssetInstanceForTasksTabService;

  @Override
  protected IGetTaskInstanceResponseModel executeInternal(
      IGetInstanceRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
      return getAssetInstanceForTasksTabService.execute(getKlassInstanceTreeStrategyModel);
  }
}
