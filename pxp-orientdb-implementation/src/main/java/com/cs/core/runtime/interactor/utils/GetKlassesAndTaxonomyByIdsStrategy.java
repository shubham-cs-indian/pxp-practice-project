package com.cs.core.runtime.interactor.utils;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.taxonomy.IGetKlassesAndTaxonomyByIds;
import com.cs.core.runtime.interactor.model.instancetree.GetSelectedKlassesAndTaxonomiesByIdsResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetSelectedKlassesAndTaxonomiesByIdsResponseModel;

@Component
public class GetKlassesAndTaxonomyByIdsStrategy extends OrientDBBaseStrategy implements IGetKlassesAndTaxonomyByIds{

  @Override
  public IGetSelectedKlassesAndTaxonomiesByIdsResponseModel execute(IGetKlassTaxonomyTreeRequestModel model)
      throws Exception
  {
    return execute(GET_KLASSES_AND_TAXONOMY_BY_IDS, model, GetSelectedKlassesAndTaxonomiesByIdsResponseModel.class);
  }
  
}
