package com.cs.core.config.strategy.usecase.attributiontaxonomy;

import com.cs.core.config.interactor.model.attributiontaxonomy.GetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.taxonomy.IGetTaxonomyRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component("getKlassAndAttributionTaxonomyStrategy")
public class GetKlassAndAttributionTaxonomyStrategy extends OrientDBBaseStrategy
    implements IGetKlassAndAttributionTaxonomyStrategy {
  
  @Override
  public IGetMasterTaxonomyWithoutKPModel execute(IGetTaxonomyRequestModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.GET_KLASS_AND_ATTRIBUTION_TAXONOMY, model,
        GetMasterTaxonomyWithoutKPModel.class);
  }
}
