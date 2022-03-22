package com.cs.di.config.strategy.cache;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.cache.IDeleteConfigDetailCacheStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import org.springframework.stereotype.Component;

@Component("deleteConfigDetailCacheStrategy") public class DeleteConfigDetailCacheStrategy
    extends OrientDBBaseStrategy implements IDeleteConfigDetailCacheStrategy {
  
  public static final String useCase = "DeleteConfigDetailCache";

  @Override public IModel execute(IIdParameterModel model) throws Exception
  {
    execute(useCase, model);
    return null;
  }

}
