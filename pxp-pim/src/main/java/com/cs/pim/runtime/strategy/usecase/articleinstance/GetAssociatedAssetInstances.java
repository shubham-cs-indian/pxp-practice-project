package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetAssociatedAssetInstancesModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetAssociatedAssetInstances;
import com.cs.core.runtime.klassinstance.IGetAssociatedAssetInstancesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAssociatedAssetInstances extends com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor<IIdParameterModel, IGetAssociatedAssetInstancesModel>
    implements IGetAssociatedAssetInstances {
  
  @Autowired IGetAssociatedAssetInstancesService getAssociatedAssetInstancesService;
  
  @Override
  protected IGetAssociatedAssetInstancesModel executeInternal(IIdParameterModel model) throws Exception
  {
    return getAssociatedAssetInstancesService.execute(model);
  }
}
