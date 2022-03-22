package com.cs.core.config.businessapi.tag;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractGetAvailableTaxonomyTags<P extends IModel, R extends IModel>
    extends AbstractGetConfigService<P, R> {
  
  protected abstract R executeGetAvailableTaxonomyTabs(IIdParameterModel model) throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {
    return (R) executeGetAvailableTaxonomyTabs((IIdParameterModel) model);
  }
}
