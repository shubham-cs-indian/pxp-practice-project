package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public abstract class AbstractGetTagTaxonomy<P extends IModel, R extends IModel>
    extends AbstractGetConfigInteractor<P, R> {
  
  protected abstract R executeGetAttributionTaxonomy(P model) throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {
    return (R) executeGetAttributionTaxonomy(model);
  }
}
