package com.cs.core.config.strategy.usecase.taxonomy;

import com.cs.core.config.interactor.model.taxonomyhierarchy.ITaxonomyHierarchyModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.TaxonomyHierarchyModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;
import org.springframework.stereotype.Component;

@Component
public class GetNonRootTaxonomiesForAllowedTypesStrategy extends OrientDBBaseStrategy
    implements IGetNonRootTaxonomiesForAllowedTypesStrategy {
  
  private static final String useCase = "GetNonRootTaxonomiesForAllowedTypes";
  
  @Override
  public ITaxonomyHierarchyModel execute(IAllowedTypesRequestModel model) throws Exception
  {
    return execute(useCase, model, TaxonomyHierarchyModel.class);
  }
}
