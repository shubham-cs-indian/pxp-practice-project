package com.cs.core.config.strategy.usecase.attributiontaxonomy;

import com.cs.core.config.interactor.model.attributiontaxonomy.GetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("createMasterTaxonomyStrategy")
public class CreateMasterTaxonomyStrategy extends OrientDBBaseStrategy
    implements ICreateMasterTaxonomyStrategy {
  
  @Override
  public IGetMasterTaxonomyWithoutKPModel execute(ICreateMasterTaxonomyModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("klassTaxonomy", model);
    return super.execute(OrientDBBaseStrategy.CREATE_MASTER_TAXONOMY, requestMap,
        GetMasterTaxonomyWithoutKPModel.class);
  }
}
