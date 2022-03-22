package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.klass.ConfigDetailsForTaxonomyHierarchyModel;
import com.cs.core.config.interactor.model.klass.IConfigDetailsForTaxonomyHierarchyModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForTaxonomyHierarchyStrategy extends OrientDBBaseStrategy
    implements IConfigDetailsForTaxonomyHierarchyStrategy {
  
  public static final String useCase = "GetConfigDetailsForTaxonomyHierarchy";
  
  @Override
  public IConfigDetailsForTaxonomyHierarchyModel execute(IIdParameterModel model) throws Exception
  {
    return execute(useCase, model, ConfigDetailsForTaxonomyHierarchyModel.class);
  }
}
