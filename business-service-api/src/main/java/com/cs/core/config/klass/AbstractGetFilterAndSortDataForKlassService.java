package com.cs.core.config.klass;

import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;

@Component
public abstract class AbstractGetFilterAndSortDataForKlassService<P extends IIdsListParameterModel, R extends IGetFilterInformationModel>
    extends AbstractGetConfigService<P, R> {
  
  protected abstract R executeGetFilterAndSortDataForArticle(P model) throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {
    return executeGetFilterAndSortDataForArticle(model);
  }
}
