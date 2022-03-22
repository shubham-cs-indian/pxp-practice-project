package com.cs.core.config.strategy.usecase.language;

import org.springframework.stereotype.Component;
import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.interactor.model.language.IDeleteLanguageRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;

@Component
public class DeleteLanguageStrategy extends OrientDBBaseStrategy
    implements IDeleteLanguageStrategy {
  
  @Override
  public IBulkDeleteReturnModel execute(IDeleteLanguageRequestModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.DELETE_LANGUAGE, model, BulkDeleteReturnModel.class);
  }
}
