package com.cs.core.config.interactor.usecase.duplicatecode;

import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IGetEntityIdsByEntityTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@SuppressWarnings("unchecked")
@Component
abstract class AbstractGetConfigEntityIdsByEntityType<P extends IGetEntityIdsByEntityTypeModel, R extends IModel>
    extends AbstractGetConfigInteractor<P, R> {
  
  protected abstract IIdsListParameterModel getConfigEntityIds(P model) throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {
    return (R) getConfigEntityIds(model);
  }
}
