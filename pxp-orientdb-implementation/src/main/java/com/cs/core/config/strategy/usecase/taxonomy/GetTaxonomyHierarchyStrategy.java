package com.cs.core.config.strategy.usecase.taxonomy;

import com.cs.core.config.interactor.model.attributiontaxonomy.ITaxonomyInformationModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.TaxonomyInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

import org.springframework.stereotype.Component;

@Component
public class GetTaxonomyHierarchyStrategy extends OrientDBBaseStrategy
    implements IGetTaxonomyHierarchyStrategy {
  
  public static final String useCase = "GetChildTaxonomiesByParentId";
  
  @Override
  public ITaxonomyInformationModel execute(IIdParameterModel model) throws Exception
  {
    return execute(useCase, model, TaxonomyInformationModel.class);
  }
}
