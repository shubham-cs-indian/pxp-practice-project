package com.cs.core.runtime.interactor.usecase.base;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetAssociatedAssetInstancesModel;
import com.cs.core.runtime.strategy.usecase.klassinstance.IGetAssociatedAssetInstancesStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unchecked")
public abstract class AbstractGetAssociatedAssetInstances<P extends IIdParameterModel, R extends IGetAssociatedAssetInstancesModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected IGetAssociatedAssetInstancesStrategy getAssociatedAssetInstancesStrategy;
  
  @Override
  protected R executeInternal(IIdParameterModel model) throws Exception
  {
    IGetAssociatedAssetInstancesModel returnModel = getAssociatedAssetInstancesStrategy
        .execute(model);
    return (R) returnModel;
  }
}
