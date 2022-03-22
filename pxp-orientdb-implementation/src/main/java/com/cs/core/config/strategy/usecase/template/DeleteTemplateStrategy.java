package com.cs.core.config.strategy.usecase.template;

import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

@Component
public class DeleteTemplateStrategy extends OrientDBBaseStrategy
    implements IDeleteTemplateStrategy {
  
  public static final String useCase = "DeleteTemplate";
  
  @Override
  public IBulkDeleteReturnModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(useCase, model, BulkDeleteReturnModel.class);
  }
}
