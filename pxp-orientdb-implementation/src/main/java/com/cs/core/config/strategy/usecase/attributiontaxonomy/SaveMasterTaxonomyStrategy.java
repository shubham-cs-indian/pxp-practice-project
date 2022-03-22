package com.cs.core.config.strategy.usecase.attributiontaxonomy;

import com.cs.core.config.interactor.model.attributiontaxonomy.GetMasterTaxonomyWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ISaveMasterTaxonomyModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("saveMasterTaxonomyStrategy")
public class SaveMasterTaxonomyStrategy extends OrientDBBaseStrategy
    implements ISaveMasterTaxonomyStrategy {
  
  @Override
  public IGetMasterTaxonomyWithoutKPStrategyResponseModel execute(ISaveMasterTaxonomyModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("klassTaxonomy", model);
    return super.execute(OrientDBBaseStrategy.SAVE_MASTER_TAXONOMY, requestMap,
        GetMasterTaxonomyWithoutKPStrategyResponseModel.class);
  }
}
