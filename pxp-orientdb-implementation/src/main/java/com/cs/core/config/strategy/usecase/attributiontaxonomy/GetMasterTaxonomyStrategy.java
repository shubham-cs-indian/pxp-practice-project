package com.cs.core.config.strategy.usecase.attributiontaxonomy;

import com.cs.core.config.interactor.model.attributiontaxonomy.GetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.taxonomy.IGetTaxonomyRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component("getMasterTaxonomyStrategy")
public class GetMasterTaxonomyStrategy extends OrientDBBaseStrategy
    implements IGetMasterTaxonomyStrategy {
  
  @Override
  public IGetMasterTaxonomyWithoutKPModel execute(IGetTaxonomyRequestModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.GET_MASTER_TAXONOMY, model,
        GetMasterTaxonomyWithoutKPModel.class);
  }
}
