package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.taxonomyhierarchy.ITaxonomyHierarchyModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.TaxonomyHierarchyModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;
import org.springframework.stereotype.Component;

@Component
public class GetAllowedTypesByBaseTypeStrategy extends OrientDBBaseStrategy
    implements IGetAllowedTypesByBaseTypeStrategy {
  
  @Override
  public ITaxonomyHierarchyModel execute(IAllowedTypesRequestModel model) throws Exception
  {
    
    return execute(GET_ALLOWED_TYPES_BY_BASE_TYPE, model, TaxonomyHierarchyModel.class);
  }
}
