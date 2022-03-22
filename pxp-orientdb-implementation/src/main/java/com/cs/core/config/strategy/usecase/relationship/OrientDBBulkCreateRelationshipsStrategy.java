package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.PluginSummaryModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("bulkCreateRelationshipsStrategy")
public class OrientDBBulkCreateRelationshipsStrategy extends OrientDBBaseStrategy
    implements IBulkCreateRelationshipsStrategy {
  
  public static final String useCase = "BulkCreateRelationships";
  
  @Override
  public IPluginSummaryModel execute(IListModel<IRelationshipModel> model) throws Exception
  {
    Map<String, Object> map = new HashMap<>();
    map.put("list", model);
    return execute(useCase, map, PluginSummaryModel.class);
  }
}
