package com.cs.core.config.strategy.usecase.attributiontaxonomy;

import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

@Component("deleteAttributionTaxonomyStrategy")
public class DeleteMasterTaxonomyStrategy extends OrientDBBaseStrategy
    implements IDeleteMasterTaxonomyStrategy {
  
  @Override
  public IBulkDeleteReturnModel execute(IIdsListParameterModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.DELETE_MASTER_TAXONOMY, model,
        BulkDeleteReturnModel.class);
  }
}
