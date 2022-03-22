package com.cs.dam.config.strategy.usecase.iconlibrary;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Component("bulkDeleteIconsStrategy")
public class BulkDeleteIconsStrategy extends OrientDBBaseStrategy implements IBulkDeleteIconsStrategy{

  @Override
  public IBulkDeleteReturnModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(BULK_DELETE_ICONS, model, BulkDeleteReturnModel.class);
  }
  
}
