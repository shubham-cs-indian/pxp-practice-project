package com.cs.core.config.strategy.usecase.taxonomy;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.CategoryInformationModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeLeafIdsStrategyModel;

import org.springframework.stereotype.Component;

@Component
public class GetTaxonomyTreeByLeafIdsStrategy extends OrientDBBaseStrategy
    implements IGetTaxonomyTreeByLeafIdsStrategy {
  
  public static final String useCase = "GetTaxonomyTreeByLeafIds";
  
  @Override
  public ICategoryInformationModel execute(IGetTaxonomyTreeLeafIdsStrategyModel model)
      throws Exception
  {
    return execute(useCase, model, CategoryInformationModel.class);
  }
}
