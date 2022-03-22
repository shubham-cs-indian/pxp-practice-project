package com.cs.core.config.interactor.usecase.klass;

import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;

@Component
public abstract class AbstractGetFilterAndSortDataForKlass<P extends IIdsListParameterModel, R extends IGetFilterInformationModel>
    extends AbstractGetConfigInteractor<P, R> {
  
  protected abstract R executeGetFilterAndSortDataForArticle(P model) throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {
    return executeGetFilterAndSortDataForArticle(model);
  }
}
