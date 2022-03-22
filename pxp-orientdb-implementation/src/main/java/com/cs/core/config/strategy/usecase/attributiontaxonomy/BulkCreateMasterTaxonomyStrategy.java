package com.cs.core.config.strategy.usecase.attributiontaxonomy;

import com.cs.core.config.interactor.model.attributiontaxonomy.BulkCreateTaxonomyResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IBulkCreateTaxonomyResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BulkCreateMasterTaxonomyStrategy extends OrientDBBaseStrategy
    implements IBulkCreateMasterTaxonomyStrategy {
  
  @Override
  public IBulkCreateTaxonomyResponseModel execute(IListModel<ICreateMasterTaxonomyModel> model)
      throws Exception
  {
    Map<String, Object> map = new HashMap<>();
    map.put("list", model.getList());
    return super.execute(OrientDBBaseStrategy.BULK_CREATE_MASTER_TAXONOMY, map,
        BulkCreateTaxonomyResponseModel.class);
  }
}
