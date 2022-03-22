package com.cs.core.config.strategy.usecase.klass;


import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.attributiontaxonomy.ITaxonomyInformationModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.TaxonomyInformationModel;
import com.cs.core.config.interactor.model.configdetails.IIdPaginationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetChildTaxonomiesByParentIdWithPaginationStrategy;

@Component
public class GetChildTaxonomiesByParentIdWithPaginationStrategy extends OrientDBBaseStrategy
    implements IGetChildTaxonomiesByParentIdWithPaginationStrategy {
  
  public static final String useCase = "GetChildTaxonomiesByParentIdWithPagination";
  
  @Override
  public ITaxonomyInformationModel execute(IIdPaginationModel model) throws Exception
  {
    return execute(useCase, model, TaxonomyInformationModel.class);
  }
  
}
