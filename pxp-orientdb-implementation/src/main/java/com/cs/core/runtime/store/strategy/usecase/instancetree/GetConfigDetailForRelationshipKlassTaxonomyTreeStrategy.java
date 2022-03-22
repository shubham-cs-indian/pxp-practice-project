package com.cs.core.runtime.store.strategy.usecase.instancetree;

import org.springframework.stereotype.Component;

import com.cs.core.base.interactor.model.IModuleEntitiesWithUserIdModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForRelationshipKlassTaxonomyResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipKlassTaxonomyResponseModel;

@Component
public class GetConfigDetailForRelationshipKlassTaxonomyTreeStrategy extends OrientDBBaseStrategy 
        implements IGetConfigDetailForRelationshipKlassTaxonomyTreeStrategy {
  
  @Override
  public IConfigDetailsForRelationshipKlassTaxonomyResponseModel execute(IModuleEntitiesWithUserIdModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAIL_FOR_RELATIONSHIP_KLASS_TAXONOMY_TREE, model, ConfigDetailsForRelationshipKlassTaxonomyResponseModel.class);
  }
  
}
