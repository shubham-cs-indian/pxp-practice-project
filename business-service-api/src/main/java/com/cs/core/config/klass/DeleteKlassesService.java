package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.klass.IBulkDeleteKlassResponseModel;
import com.cs.core.config.klass.IDeleteKlassService;
import com.cs.core.config.strategy.usecase.klass.IDeleteKlassStrategy;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteKlassesService extends
    AbstractDeleteKlassesService<IIdsListParameterModel, IBulkDeleteReturnModel> implements IDeleteKlassService {
  
  @Autowired
  protected IDeleteKlassStrategy neo4jDeleteKlassesStrategy;
  
  @Override
  protected String getKlassType()
  {
    return Constants.PROJECT_KLASS_TYPE;
  }
  
  @Override
  protected IBulkDeleteKlassResponseModel executeBulkDelete(IIdsListParameterModel model)
      throws Exception
  {
    return neo4jDeleteKlassesStrategy.execute(model);
  }
}
