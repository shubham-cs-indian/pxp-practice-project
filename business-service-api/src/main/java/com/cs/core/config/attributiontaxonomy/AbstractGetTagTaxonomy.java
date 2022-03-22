package com.cs.core.config.attributiontaxonomy;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractGetTagTaxonomy<P extends IModel, R extends IModel>
    extends AbstractGetConfigService<P, R> {
  
  protected abstract R executeGetAttributionTaxonomy(P model) throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {
    return (R) executeGetAttributionTaxonomy(model);
  }
}
