package com.cs.core.config.strategy.usecase.klass;


import org.springframework.stereotype.Service;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.klassinstance.IReferencedTypesAndTaxonomiesModel;
import com.cs.core.runtime.interactor.model.klassinstance.ITypesTaxonomiesModel;
import com.cs.core.runtime.interactor.model.klassinstance.ReferencedTypesAndTxonomiesModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForTypesAndTaxonomiesOfContentStrategy;

@Service
public class GetConfigDetailsForTypesAndTaxonomiesOfContentStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForTypesAndTaxonomiesOfContentStrategy {
  
  @Override
  public IReferencedTypesAndTaxonomiesModel execute(ITypesTaxonomiesModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_TYPES_AND_TAXONOMIES_OF_CONTENT, model,
        ReferencedTypesAndTxonomiesModel.class);
  }
}
