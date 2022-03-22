package com.cs.core.config.strategy.usecase.taxonomy;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.attributiontaxonomy.GetAttributionTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetAttributionTaxonomyModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.IGetAttributionTaxonomiesForQuickListStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Component("getArticleTaxonomiesForQuicklistStrategy")
public class GetAttributionTaxonomiesForQuickListStrategy extends OrientDBBaseStrategy
    implements IGetAttributionTaxonomiesForQuickListStrategy {
  
  public static final String useCase = "GetTaxonomiesForQuicklist";
  
  @Override
  public IGetAttributionTaxonomyModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(useCase, model, GetAttributionTaxonomyModel.class);
  }
}
