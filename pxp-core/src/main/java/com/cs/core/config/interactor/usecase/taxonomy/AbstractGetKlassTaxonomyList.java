package com.cs.core.config.interactor.usecase.taxonomy;

import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.taxonomy.IGetArticleTaxonomyListModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Component
public abstract class AbstractGetKlassTaxonomyList<P extends IModel, R extends IGetArticleTaxonomyListModel>
    extends AbstractGetConfigInteractor<P, R> {
  
  protected abstract R executeGetArticleTaxonomyList(P model) throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {
    return executeGetArticleTaxonomyList(null);
  }
}
