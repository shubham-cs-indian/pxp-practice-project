package com.cs.core.runtime.store.strategy.usecase.instancetree;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsKlassTaxonomyTreeResponseModel;

@Component
public class GetConfigDetailForKlassTaxonomyTreeStrategy extends OrientDBBaseStrategy implements IGetConfigDetailForKlassTaxonomyTreeStrategy {
  
  @Override
  public IConfigDetailsKlassTaxonomyTreeResponseModel execute(IConfigDetailsForGetKlassTaxonomyTreeRequestModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAIL_FOR_KLASS_TAXONOMY_TREE, model, ConfigDetailsKlassTaxonomyTreeResponseModel.class);
  }
  
}
