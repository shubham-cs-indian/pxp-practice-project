package com.cs.core.config.strategy.usecase.attributiontaxonomy;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.CategoryTreeInformationModel;
import com.cs.core.config.interactor.model.datarule.ICategoryTreeInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Component
public class GetTaxonomiesStrategy extends OrientDBBaseStrategy implements IGetTaxonomiesStrategy {
  
  @Override
  public ICategoryTreeInformationModel execute(IIdsListParameterModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.GET_TAXONOMIES, model,
        CategoryTreeInformationModel.class);
  }
}
