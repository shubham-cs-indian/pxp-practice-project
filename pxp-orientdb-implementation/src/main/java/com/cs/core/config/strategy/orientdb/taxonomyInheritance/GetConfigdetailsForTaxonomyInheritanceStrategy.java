package com.cs.core.config.strategy.orientdb.taxonomyInheritance;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.GetInheritenceTaxonomyIdsResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetInheritanceTaxonomyIdsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailForTaxonomyInheritanceStrategy;

@Component
public class GetConfigdetailsForTaxonomyInheritanceStrategy extends OrientDBBaseStrategy implements  IGetConfigDetailForTaxonomyInheritanceStrategy {

  @Override
  public IGetInheritanceTaxonomyIdsResponseModel execute(IIdsListParameterModel model) throws Exception
  {
    return super.execute(GET_CONFIG_DETAILS_FOR_TAXONOMY_INHEERITANCE, model, GetInheritenceTaxonomyIdsResponseModel.class);
  }
}

