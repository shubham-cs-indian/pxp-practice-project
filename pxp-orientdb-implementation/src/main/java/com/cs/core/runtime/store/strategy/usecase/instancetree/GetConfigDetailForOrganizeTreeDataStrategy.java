package com.cs.core.runtime.store.strategy.usecase.instancetree;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsOrganizeTreeDataResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsOrganizeTreeDataResponseModel;

@Component
public class GetConfigDetailForOrganizeTreeDataStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailForOrganizeTreeDataStrategy {
  
  @Override
  public IConfigDetailsOrganizeTreeDataResponseModel execute(
      IConfigDetailsForGetKlassTaxonomyTreeRequestModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAIL_FOR_ORGANIZE_TREE_DATA, model,
        ConfigDetailsOrganizeTreeDataResponseModel.class);
  }
  
}
