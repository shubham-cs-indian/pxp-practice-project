package com.cs.core.config.strategy.usecase.taxonomy;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.taxonomyhierarchy.ITaxonomyHierarchyModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.TaxonomyHierarchyModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;

@Component
public class GetAllTaxonomiesForAllowedTypesStrategy extends OrientDBBaseStrategy
    implements IGetAllTaxonomiesForAllowedTypesStrategy {
  
  private static final String useCase = "GetAllTaxonomiesForAllowedTypes";
  
  @Override
  public ITaxonomyHierarchyModel execute(IAllowedTypesRequestModel model) throws Exception
  {
    return execute(useCase, model, TaxonomyHierarchyModel.class);
  }
}
