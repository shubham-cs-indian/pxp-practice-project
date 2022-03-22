package com.cs.core.config.strategy.usecase.staticcollection;

import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

@Component
public class DeleteStaticCollectionNodeStrategy extends OrientDBBaseStrategy
    implements IDeleteStaticCollectionNodeStrategy {
  
  @Override
  public IBulkDeleteReturnModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(DELETE_COLLECTION_NODE, model, BulkDeleteReturnModel.class);
  }
}
